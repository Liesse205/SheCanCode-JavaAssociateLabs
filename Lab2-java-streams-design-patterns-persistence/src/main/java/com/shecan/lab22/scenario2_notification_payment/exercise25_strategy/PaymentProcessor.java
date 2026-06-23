package com.shecan.lab22.scenario2_notification_payment.exercise25_strategy;

public class PaymentProcessor {
    private PaymentStrategy strategy;

    public PaymentProcessor(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public PaymentResult processPayment(PaymentRequest request) {
        if (strategy == null) {
            throw new IllegalStateException("No payment strategy set");
        }
        return strategy.process(request);
    }

    public PaymentStrategy getStrategy() {
        return strategy;
    }
}