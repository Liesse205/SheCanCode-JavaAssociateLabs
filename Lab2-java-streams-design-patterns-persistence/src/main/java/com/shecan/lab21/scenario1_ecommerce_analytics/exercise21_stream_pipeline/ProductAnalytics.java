package com.shecan.lab21.scenario1_ecommerce_analytics.exercise21_stream_pipeline;

import com.shecan.lab21.scenario1_ecommerce_analytics.model.LineItem;
import com.shecan.lab21.scenario1_ecommerce_analytics.model.Order;
import com.shecan.lab21.scenario1_ecommerce_analytics.model.Product;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductAnalytics {

    /**
     * Exercise 2.1 - Part 1 & 2:
     * Flattens all line items from orders, filters quantity > 5,
     * maps to revenue, and sums total revenue using reduce()
     */
    public double calculateTotalRevenueFromLargeOrders(List<Order> orders) {
        return orders.stream()
                .flatMap(order -> order.getLineItems().stream())  // Flatten to LineItem stream
                .filter(item -> item.getQuantity() > 5)           // Only quantity > 5
                .mapToDouble(LineItem::getRevenue)               // Map to revenue
                .reduce(0.0, Double::sum);                       // Sum using reduce
    }

    /**
     * Exercise 2.1 - Part 3:
     * Returns top N products by total revenue using groupingBy
     */
    public List<Map.Entry<Product, Double>> topNProductsByRevenue(List<Order> orders, int n) {
        return orders.stream()
                .flatMap(order -> order.getLineItems().stream())
                .collect(Collectors.groupingBy(
                        LineItem::getProduct,                    // Group by Product
                        Collectors.summingDouble(LineItem::getRevenue) // Sum revenue per product
                ))
                .entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue())) // Sort descending
                .limit(n)                                         // Take top N
                .collect(Collectors.toList());
    }

    /**
     * Helper method to get all line items as a flat list
     */
    public List<LineItem> getAllLineItemsFlattened(List<Order> orders) {
        return orders.stream()
                .flatMap(order -> order.getLineItems().stream())
                .collect(Collectors.toList());
    }
}