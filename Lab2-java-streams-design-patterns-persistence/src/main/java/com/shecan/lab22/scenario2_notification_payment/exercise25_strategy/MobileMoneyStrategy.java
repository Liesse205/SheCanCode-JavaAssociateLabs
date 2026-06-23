package com.shecan.lab22.scenario2_notification_payment.exercise25_strategy;

public class MobileMoneyStrategy implements PaymentStrategy {
    private static final double FEE_PERCENTAGE = 0.015; // 1.5% fee
    private static final double MINIMUM_FEE = 0.50;
    private static final double MINIMUM_AMOUNT = 0.50;
    private static final String VALID_PREFIX = "MM-";

    @Override
    public PaymentResult process(PaymentRequest request) {
        // Validation
        if (request.getAccountId() == null || request.getAccountId().trim().isEmpty()) {
            return new PaymentResult(false, "Mobile Money: Account ID is required", 0, 0);
        }
        
        if (!request.getAccountId().startsWith(VALID_PREFIX)) {
            return new PaymentResult(false, 
                "Mobile Money: Account ID must start with '" + VALID_PREFIX + "'", 0, 0);
        }
        
        if (request.getAmount() < MINIMUM_AMOUNT) {
            return new PaymentResult(false, 
                "Mobile Money: Amount must be at least " + MINIMUM_AMOUNT, 0, 0);
        }

        // Calculate fee (percentage with minimum)
        double fee = request.getAmount() * FEE_PERCENTAGE;
        if (fee < MINIMUM_FEE) {
            fee = MINIMUM_FEE;
        }
        double totalAmount = request.getAmount() + fee;

        return new PaymentResult(true, 
            "Mobile Money: Payment processed successfully for " + request.getAccountId(),
            fee, totalAmount);
    }
}