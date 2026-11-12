package com.acme.orderservice.payment;

import java.math.BigDecimal;

/**
 * Client du prestataire de paiement externe.
 */
public interface PaymentClient {

    /**
     * Débite la carte du montant demandé.
     *
     * @throws PaymentException si le paiement est refusé
     */
    void charge(String cardNumber, BigDecimal amount);
}
