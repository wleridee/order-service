package com.acme.orderservice.order;

import com.acme.orderservice.payment.PaymentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Order create(@RequestBody OrderRequest request) {
        try {
            return orderService.placeOrder(request);
        } catch (Exception e) {
            if (e instanceof PaymentException) {
                PaymentException pe = (PaymentException) e;
                log.error("Paiement refusé : " + pe.getMessage());
            } else {
                e.printStackTrace();
            }
            return null;
        }
    }

    @GetMapping("/search")
    public List<Order> search(@RequestParam String customer) {
        return orderService.search(customer);
    }
}
