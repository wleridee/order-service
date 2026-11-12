package com.acme.orderservice.payment;

public class PaymentException extends RuntimeException {

    public PaymentException(String message) {
        super(message);
    }
}
