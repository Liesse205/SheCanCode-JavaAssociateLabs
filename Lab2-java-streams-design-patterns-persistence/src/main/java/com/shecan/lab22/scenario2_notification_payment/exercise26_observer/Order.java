package com.shecan.lab22.scenario2_notification_payment.exercise26_observer;

public class Order {
    private final String orderId;
    private final String customerName;
    private final double amount;

    public Order(String orderId, String customerName, double amount) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.amount = amount;
    }

    public String getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public double getAmount() { return amount; }

    @Override
    public String toString() {
        return "Order{orderId='" + orderId + "', customerName='" + customerName + "', amount=" + amount + "}";
    }
}