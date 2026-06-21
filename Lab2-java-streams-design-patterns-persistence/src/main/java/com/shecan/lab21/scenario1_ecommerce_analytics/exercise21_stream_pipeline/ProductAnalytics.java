package com.shecan.lab21.scenario1_ecommerce_analytics.exercise21_stream_pipeline;

import com.shecan.lab21.scenario1_ecommerce_analytics.model.LineItem;
import com.shecan.lab21.scenario1_ecommerce_analytics.model.Order;
import com.shecan.lab21.scenario1_ecommerce_analytics.model.Product;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductAnalytics {

    public double calculateTotalRevenueFromLargeOrders(List<Order> orders) {
        return orders.stream()
                .flatMap(order -> order.getLineItems().stream())
                .filter(item -> item.getQuantity() > 5)
                .mapToDouble(LineItem::getRevenue)
                .reduce(0.0, Double::sum);
    }

    public List<Map.Entry<Product, Double>> topNProductsByRevenue(List<Order> orders, int n) {
        return orders.stream()
                .flatMap(order -> order.getLineItems().stream())
                .collect(Collectors.groupingBy(
                        LineItem::getProduct,
                        Collectors.summingDouble(LineItem::getRevenue)
                ))
                .entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .limit(n)
                .collect(Collectors.toList());
    }

    public List<LineItem> getAllLineItemsFlattened(List<Order> orders) {
        return orders.stream()
                .flatMap(order -> order.getLineItems().stream())
                .collect(Collectors.toList());
    }
}