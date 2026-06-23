package com.shecan.lab22.scenario2_notification_payment.exercise26_observer;

public interface OrderObserver {
    void onEvent(Order order, OrderEvent event);
}