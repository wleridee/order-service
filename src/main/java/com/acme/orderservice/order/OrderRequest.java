package com.acme.orderservice.order;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class OrderRequest {

    private String customerEmail;

    private String cardNumber;

    private List<Long> productIds;

    private Optional<String> couponCode = Optional.empty();

    public OrderRequest() {
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }

    public Optional<String> getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(Optional<String> couponCode) {
        this.couponCode = couponCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderRequest that = (OrderRequest) o;
        return Objects.equals(customerEmail, that.customerEmail)
                && Objects.equals(cardNumber, that.cardNumber)
                && Objects.equals(productIds, that.productIds)
                && Objects.equals(couponCode, that.couponCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerEmail, cardNumber, productIds, couponCode);
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "customerEmail='" + customerEmail + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", productIds=" + productIds +
                ", couponCode=" + couponCode +
                '}';
    }
}
