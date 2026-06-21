package com.shecan.lab21.scenario1_ecommerce_analytics.exercise21_stream_pipeline;

import com.shecan.lab21.scenario1_ecommerce_analytics.model.LineItem;
import com.shecan.lab21.scenario1_ecommerce_analytics.model.Order;
import com.shecan.lab21.scenario1_ecommerce_analytics.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.shecan.common.testdata.OrderTestData.sampleOrders;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductAnalyticsTest {

    private ProductAnalytics analytics;
    private List<Order> orders;

    @BeforeEach
    void setUp() {
        analytics = new ProductAnalytics();
        orders = sampleOrders();
    }

    @Test
    void getAllLineItemsFlattened_shouldReturnAllLineItems() {
        List<LineItem> allItems = analytics.getAllLineItemsFlattened(orders);
        
        assertThat(allItems).hasSize(9);
        assertThat(allItems).extracting("product.name")
                .containsExactly("Laptop", "Smartphone", "Java Book", 
                                "Smartphone", "Headphones", "T-Shirt",
                                "Laptop", "Java Book", "T-Shirt");
    }

    @Test
    void calculateTotalRevenueFromLargeOrders_shouldSumRevenueForQuantityGreaterThan5() {
        double totalRevenue = analytics.calculateTotalRevenueFromLargeOrders(orders);
        assertEquals(8269.63, totalRevenue, 0.001);
    }

    @Test
    void topNProductsByRevenue_shouldReturnTop2Products() {
        List<Map.Entry<Product, Double>> top2 = analytics.topNProductsByRevenue(orders, 2);
        
        assertThat(top2).hasSize(2);
        assertThat(top2.get(0).getKey().getName()).isEqualTo("Smartphone");
        assertEquals(7699.89, top2.get(0).getValue(), 0.001);
        
        assertThat(top2.get(1).getKey().getName()).isEqualTo("Laptop");
        assertEquals(4999.95, top2.get(1).getValue(), 0.001);
    }

    @Test
    void topNProductsByRevenue_shouldReturnTop3Products() {
        List<Map.Entry<Product, Double>> top3 = analytics.topNProductsByRevenue(orders, 3);
        
        assertThat(top3).hasSize(3);
        assertThat(top3.get(0).getKey().getName()).isEqualTo("Smartphone");
        assertEquals(7699.89, top3.get(0).getValue(), 0.001);
        
        assertThat(top3.get(1).getKey().getName()).isEqualTo("Laptop");
        assertEquals(4999.95, top3.get(1).getValue(), 0.001);
        
        assertThat(top3.get(2).getKey().getName()).isEqualTo("Headphones");
        assertEquals(629.93, top3.get(2).getValue(), 0.001);
    }

    @Test
    void topNProductsByRevenue_shouldHandleNGreaterThanProductCount() {
        List<Map.Entry<Product, Double>> top10 = analytics.topNProductsByRevenue(orders, 10);
        
        assertThat(top10).hasSize(5);
        assertThat(top10).extracting(entry -> entry.getKey().getName())
                .containsExactly("Smartphone", "Laptop", "Headphones", "Java Book", "T-Shirt");
    }

    @Test
    void calculateTotalRevenueFromLargeOrders_shouldReturnZeroForEmptyOrders() {
        List<Order> emptyOrders = List.of();
        double totalRevenue = analytics.calculateTotalRevenueFromLargeOrders(emptyOrders);
        assertEquals(0.0, totalRevenue, 0.001);
    }
}