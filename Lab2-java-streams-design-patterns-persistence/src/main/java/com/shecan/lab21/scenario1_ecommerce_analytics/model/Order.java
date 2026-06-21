package com.shecan.lab21.scenario1_ecommerce_analytics.model;

import java.util.List;

public class Order {
    private final String orderId;
    private final List<LineItem> lineItems;
    private final boolean delivered;

    public Order(String orderId, List<LineItem> lineItems, boolean delivered) {
        this.orderId = orderId;
        this.lineItems = lineItems;
        this.delivered = delivered;
    }

    public String getOrderId() { return orderId; }
    public List<LineItem> getLineItems() { return lineItems; }
    public boolean isDelivered() { return delivered; }

    @Override
    public String toString() {
        return "Order{orderId='" + orderId + "', items=" + lineItems.size() + ", delivered=" + delivered + "}";
    }
}