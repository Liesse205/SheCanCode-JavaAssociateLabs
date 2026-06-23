package com.shecan.lab22.scenario2_notification_payment.exercise26_observer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditLogger implements OrderObserver {
    private static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void onEvent(Order order, OrderEvent event) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String message = "[AUDIT LOGGER] " + timestamp + 
                         " - Order " + order.getOrderId() + 
                         " event: " + event;
        System.out.println(message);
    }
}