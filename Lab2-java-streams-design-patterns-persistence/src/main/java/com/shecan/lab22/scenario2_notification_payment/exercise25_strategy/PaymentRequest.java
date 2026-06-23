package com.shecan.lab22.scenario2_notification_payment.exercise25_strategy;

public class PaymentRequest {
    private final String accountId;
    private final double amount;
    private final String currency;

    public PaymentRequest(String accountId, double amount, String currency) {
        this.accountId = accountId;
        this.amount = amount;
        this.currency = currency;
    }

    public String getAccountId() { return accountId; }
    public double getAmount() { return amount; }
    public String getCurrency() { return currency; }

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "accountId='" + accountId + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                '}';
    }
}