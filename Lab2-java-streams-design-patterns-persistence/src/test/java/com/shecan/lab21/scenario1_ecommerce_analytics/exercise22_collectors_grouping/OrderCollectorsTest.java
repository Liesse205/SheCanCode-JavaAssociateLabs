package com.shecan.lab21.scenario1_ecommerce_analytics.exercise22_collectors_grouping;

import com.shecan.lab21.scenario1_ecommerce_analytics.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.shecan.common.testdata.OrderTestData.sampleOrders;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderCollectorsTest {

    private OrderCollectors orderCollectors;
    private List<Order> orders;

    @BeforeEach
    void setUp() {
        orderCollectors = new OrderCollectors();
        orders = sampleOrders();
    }

    @Test
    void countItemsByCategory_shouldGroupAndCountItemsPerCategory() {
        Map<String, Long> categoryCounts = orderCollectors.countItemsByCategory(orders);
        
        assertThat(categoryCounts).hasSize(3);
        assertEquals(5L, categoryCounts.get("Electronics"));
        assertEquals(2L, categoryCounts.get("Books"));
        assertEquals(2L, categoryCounts.get("Clothing"));
    }

    @Test
    void partitionOrdersByDeliveryStatus_shouldSplitIntoDeliveredAndPending() {
        Map<Boolean, List<Order>> partitioned = orderCollectors.partitionOrdersByDeliveryStatus(orders);
        
        assertThat(partitioned).containsKeys(true, false);
        
        List<Order> delivered = partitioned.get(true);
        assertThat(delivered).hasSize(2);
        assertThat(delivered).extracting(Order::getOrderId)
                .containsExactly("ORD-001", "ORD-003");
        
        List<Order> pending = partitioned.get(false);
        assertThat(pending).hasSize(1);
        assertThat(pending).extracting(Order::getOrderId)
                .containsExactly("ORD-002");
    }

    @Test
    void getProductAveragePrice_shouldReturnAveragePricePerProduct() {
        Map<String, Double> avgPrices = orderCollectors.getProductAveragePrice(orders);
        
        assertThat(avgPrices).hasSize(5);
        assertEquals(999.99, avgPrices.get("P001"), 0.001);
        assertEquals(699.99, avgPrices.get("P002"), 0.001);
        assertEquals(49.99, avgPrices.get("P003"), 0.001);
        assertEquals(89.99, avgPrices.get("P004"), 0.001);
        assertEquals(19.99, avgPrices.get("P005"), 0.001);
    }

    @Test
    void countItemsByCategory_shouldReturnEmptyMapForEmptyOrders() {
        List<Order> emptyOrders = List.of();
        Map<String, Long> categoryCounts = orderCollectors.countItemsByCategory(emptyOrders);
        assertThat(categoryCounts).isEmpty();
    }

    @Test
    void partitionOrdersByDeliveryStatus_shouldReturnEmptyListsForEmptyOrders() {
        List<Order> emptyOrders = List.of();
        Map<Boolean, List<Order>> partitioned = orderCollectors.partitionOrdersByDeliveryStatus(emptyOrders);
        assertThat(partitioned.get(true)).isEmpty();
        assertThat(partitioned.get(false)).isEmpty();
    }

    @Test
    void getProductAveragePrice_shouldReturnEmptyMapForEmptyOrders() {
        List<Order> emptyOrders = List.of();
        Map<String, Double> avgPrices = orderCollectors.getProductAveragePrice(emptyOrders);
        assertThat(avgPrices).isEmpty();
    }
}