package com.shecan.lab22.scenario2_notification_payment.exercise25_strategy;

public class PaymentResult {
    private final boolean success;
    private final String message;
    private final double fee;
    private final double totalAmount;

    public PaymentResult(boolean success, String message, double fee, double totalAmount) {
        this.success = success;
        this.message = message;
        this.fee = fee;
        this.totalAmount = totalAmount;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public double getFee() { return fee; }
    public double getTotalAmount() { return totalAmount; }

    @Override
    public String toString() {
        return "PaymentResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", fee=" + fee +
                ", totalAmount=" + totalAmount +
                '}';
    }
}