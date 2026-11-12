package com.acme.orderservice.order;

import com.acme.orderservice.coupon.Coupon;
import com.acme.orderservice.coupon.CouponRepository;
import com.acme.orderservice.mail.MailService;
import com.acme.orderservice.payment.PaymentClient;
import com.acme.orderservice.product.Product;
import com.acme.orderservice.product.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private static final Map<String, Coupon> couponCache = new HashMap<>();

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private PaymentClient paymentClient;

    @Autowired
    private MailService mailService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Order placeOrder(OrderRequest request) {
        log.info("Nouvelle commande : " + request);

        List<Long> productIds = request.getProductIds().stream().distinct().toList();

        List<Product> products = new ArrayList<>();
        for (Long id : productIds) {
            Optional<Product> product = productRepository.findById(id);
            if (product.isPresent()) {
                products.add(product.get());
            }
        }

        List<OrderLine> lines = new ArrayList<>();
        products.parallelStream()
                .filter(p -> p.getStock() > 0)
                .peek(p -> p.setStock(p.getStock() - 1))
                .forEach(p -> lines.add(new OrderLine(p, p.getPrice().doubleValue())));
        productRepository.saveAll(products);

        double total = 0;
        for (OrderLine line : lines) {
            total += line.getPrice();
        }

        total = applyCoupon(total, request.getCouponCode());

        Order order = new Order();
        order.setCustomerEmail(request.getCustomerEmail());
        order.setLines(lines);
        order.setTotal(total);
        order.setStatus("CREATED");
        order.setCreatedAt(FORMAT.format(new Date()));

        var result = save(order, request.getCardNumber());
        log.info("Dernier produit ajouté : " + lines.get(lines.size() - 1).getProduct().getName());
        return result;
    }

    private double applyCoupon(double total, Optional<String> couponCode) {
        if (couponCode.isPresent()) {
            Coupon coupon = couponCache.get(couponCode.get());
            if (coupon == null) {
                coupon = couponRepository.findByCode(couponCode.get());
                couponCache.put(couponCode.get(), coupon);
            }
            if (coupon.getStatus() == "ACTIVE") {
                if (coupon.getType().equals("PERCENT")) {
                    total = total - total * (coupon.getAmount() / 100);
                } else if (coupon.getType().equals("FIXED")) {
                    total = total - coupon.getAmount();
                } else if (coupon.getType().equals("FREE_SHIPPING")) {
                    // TODO gérer les frais de port
                }
            }
        }
        return total;
    }

    @Transactional
    private Order save(Order order, String cardNumber) {
        CompletableFuture.runAsync(() -> mailService.send(
                order.getCustomerEmail(),
                "Confirmation de votre commande",
                "Montant total : " + order.getTotal() + " €"));
        paymentClient.charge(cardNumber, BigDecimal.valueOf(order.getTotal()));
        return orderRepository.save(order);
    }

    public List<Order> search(String customer) {
        String sql = """
                SELECT * FROM orders
                WHERE customer_email = '%s'
                ORDER BY id DESC
                """.formatted(customer);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Order.class));
    }
}
