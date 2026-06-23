package com.shecan.lab22.scenario2_notification_payment.exercise26_observer;

public class InventoryUpdater implements OrderObserver {
    @Override
    public void onEvent(Order order, OrderEvent event) {
        String message = "[INVENTORY UPDATER] Order " + order.getOrderId() + 
                         " amount: $" + order.getAmount() + 
                         " has event: " + event;
        System.out.println(message);
    }
}