package com.shecan.lab22.scenario2_notification_payment.exercise25_strategy;

public interface PaymentStrategy {
    PaymentResult process(PaymentRequest request);
}