package com.shecan.lab22.scenario2_notification_payment.exercise26_observer;

public class EmailNotifier implements OrderObserver {
    @Override
    public void onEvent(Order order, OrderEvent event) {
        String message = "[EMAIL NOTIFIER] Order " + order.getOrderId() + 
                         " for " + order.getCustomerName() + 
                         " has event: " + event;
        System.out.println(message);
    }
}