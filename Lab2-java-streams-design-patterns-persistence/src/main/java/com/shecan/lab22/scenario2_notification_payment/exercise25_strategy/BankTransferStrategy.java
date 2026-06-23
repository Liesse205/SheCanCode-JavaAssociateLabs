package com.shecan.lab22.scenario2_notification_payment.exercise25_strategy;

public class BankTransferStrategy implements PaymentStrategy {
    private static final double FIXED_FEE = 5.0; // $5 fixed fee
    private static final double MINIMUM_AMOUNT = 10.0;
    private static final double MAXIMUM_AMOUNT = 10000.0;

    @Override
    public PaymentResult process(PaymentRequest request) {
        // Validation
        if (request.getAccountId() == null || request.getAccountId().trim().isEmpty()) {
            return new PaymentResult(false, "Bank Transfer: Account ID is required", 0, 0);
        }
        
        if (request.getAmount() < MINIMUM_AMOUNT) {
            return new PaymentResult(false, 
                "Bank Transfer: Amount must be at least " + MINIMUM_AMOUNT, 0, 0);
        }
        
        if (request.getAmount() > MAXIMUM_AMOUNT) {
            return new PaymentResult(false, 
                "Bank Transfer: Amount cannot exceed " + MAXIMUM_AMOUNT, 0, 0);
        }

        // Calculate fee (fixed fee)
        double fee = FIXED_FEE;
        double totalAmount = request.getAmount() + fee;

        return new PaymentResult(true, 
            "Bank Transfer: Payment processed successfully for " + request.getAccountId(),
            fee, totalAmount);
    }
}