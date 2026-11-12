package com.acme.orderservice.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    void placeOrder() {
        OrderRequest request = new OrderRequest();
        request.setCustomerEmail("jean.dupont@example.com");
        request.setCardNumber("4111111111111111");
        request.setProductIds(List.of(1L, 2L));

        Order order = orderService.placeOrder(request);

        assertNotNull(order.getId());
        assertEquals(109.80, order.getTotal(), 0.01);
    }
}
