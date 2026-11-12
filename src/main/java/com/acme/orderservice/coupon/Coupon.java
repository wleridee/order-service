package com.acme.orderservice.coupon;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    // PERCENT, FIXED ou FREE_SHIPPING
    private String type;

    private int amount;

    // ACTIVE ou EXPIRED
    private String status;

    public Coupon() {
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }
}
