package com.shecan.lab22.scenario2_notification_payment.exercise26_observer;

import java.util.ArrayList;
import java.util.List;

public class OrderEventBus {
    private final List<OrderObserver> observers = new ArrayList<>();

    public void subscribe(OrderObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("Observer cannot be null");
        }
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("[EVENT BUS] Subscribed: " + observer.getClass().getSimpleName());
        }
    }

    public void unsubscribe(OrderObserver observer) {
        if (observers.remove(observer)) {
            System.out.println("[EVENT BUS] Unsubscribed: " + observer.getClass().getSimpleName());
        }
    }

    public void publish(Order order, OrderEvent event) {
        System.out.println();
        System.out.println("[EVENT BUS] Publishing event: " + event + " for order: " + order.getOrderId());
        System.out.println("------------------------------------------------------------");
        
        for (OrderObserver observer : observers) {
            observer.onEvent(order, event);
        }
        
        System.out.println("------------------------------------------------------------");
        System.out.println("[EVENT BUS] Event published to " + observers.size() + " observers");
    }

    public int getObserverCount() {
        return observers.size();
    }

    public List<OrderObserver> getObservers() {
        return new ArrayList<>(observers);
    }

    public void clear() {
        observers.clear();
        System.out.println("[EVENT BUS] All observers cleared");
    }
}