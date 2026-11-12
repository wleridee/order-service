package com.acme.orderservice.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Simulation du prestataire de paiement (environnement local).
 * Les cartes commençant par "0000" sont refusées.
 */
@Component
public class FakePaymentClient implements PaymentClient {

    private static final Logger log = LoggerFactory.getLogger(FakePaymentClient.class);

    @Override
    public void charge(String cardNumber, BigDecimal amount) {
        simulateNetworkLatency();
        if (cardNumber == null || cardNumber.startsWith("0000")) {
            throw new PaymentException("Paiement refusé");
        }
        log.info("Paiement accepté pour un montant de {}", amount);
    }

    private void simulateNetworkLatency() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
