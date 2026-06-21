package com.shecan.lab21.scenario1_ecommerce_analytics.exercise22_collectors_grouping;

import com.shecan.lab21.scenario1_ecommerce_analytics.model.LineItem;
import com.shecan.lab21.scenario1_ecommerce_analytics.model.Order;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderCollectors {

    public Map<String, Long> countItemsByCategory(List<Order> orders) {
        return orders.stream()
                .flatMap(order -> order.getLineItems().stream())
                .collect(Collectors.groupingBy(
                        LineItem::getCategory,
                        Collectors.counting()
                ));
    }

    public Map<Boolean, List<Order>> partitionOrdersByDeliveryStatus(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.partitioningBy(Order::isDelivered));
    }

    public Map<String, Double> getProductAveragePrice(List<Order> orders) {
        return orders.stream()
                .flatMap(order -> order.getLineItems().stream())
                .collect(Collectors.toMap(
                        item -> item.getProduct().getId(),
                        item -> item.getProduct().getPrice(),
                        (price1, price2) -> (price1 + price2) / 2
                ));
    }
}