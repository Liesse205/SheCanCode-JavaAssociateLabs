package com.shecan.lab22.scenario2_notification_payment.exercise25_strategy;

public class CreditCardStrategy implements PaymentStrategy {
    private static final double FEE_PERCENTAGE = 0.025; // 2.5% fee
    private static final double MINIMUM_AMOUNT = 1.0;

    @Override
    public PaymentResult process(PaymentRequest request) {
        // Validation
        if (request.getAccountId() == null || request.getAccountId().trim().isEmpty()) {
            return new PaymentResult(false, "Credit Card: Account ID is required", 0, 0);
        }
        
        if (request.getAmount() < MINIMUM_AMOUNT) {
            return new PaymentResult(false, 
                "Credit Card: Amount must be at least " + MINIMUM_AMOUNT, 0, 0);
        }

        // Calculate fee
        double fee = request.getAmount() * FEE_PERCENTAGE;
        double totalAmount = request.getAmount() + fee;

        return new PaymentResult(true, 
            "Credit Card: Payment processed successfully for " + request.getAccountId(),
            fee, totalAmount);
    }
}