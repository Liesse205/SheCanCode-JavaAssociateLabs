package com.shecan.lab21.scenario1_ecommerce_analytics.exercise23_custom_collector;

import com.shecan.lab21.scenario1_ecommerce_analytics.exercise21_stream_pipeline.ProductAnalytics;
import com.shecan.lab21.scenario1_ecommerce_analytics.model.LineItem;
import com.shecan.lab21.scenario1_ecommerce_analytics.model.Order;
import com.shecan.lab21.scenario1_ecommerce_analytics.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.shecan.common.testdata.LineItemTestData.*;
import static com.shecan.common.testdata.OrderTestData.sampleOrders;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomRevenueCollectorTest {

    private List<Order> orders;

    @BeforeEach
    void setUp() {
        orders = sampleOrders();
    }

    // ===== Exercise 2.3 Part 1: Custom Collector =====

    @Test
    void customCollector_shouldAccumulateCorrectRevenueReport() {
        // Given: All line items from sample orders
        List<LineItem> allItems = orders.stream()
                .flatMap(order -> order.getLineItems().stream())
                .collect(Collectors.toList());

        // When: Using custom collector
        RevenueReport report = allItems.stream()
                .collect(new CustomRevenueCollector());

        // Then
        assertEquals(9L, report.getItemCount());
        
        double expectedTotalRevenue = 1999.98 + 6999.90 + 149.97 + 
                                      699.99 + 629.93 + 239.88 + 
                                      2999.97 + 399.92 + 99.95;
        assertEquals(expectedTotalRevenue, report.getTotalRevenue(), 0.001);
        assertEquals(6999.90, report.getMaxSingleItemRevenue(), 0.001);
    }

    @Test
    void customCollector_shouldWorkWithEmptyStream() {
        List<LineItem> emptyItems = List.of();

        RevenueReport report = emptyItems.stream()
                .collect(new CustomRevenueCollector());

        assertEquals(0L, report.getItemCount());
        assertEquals(0.0, report.getTotalRevenue(), 0.001);
        assertEquals(0.0, report.getMaxSingleItemRevenue(), 0.001);
    }

    @Test
    void customCollector_shouldHandleSingleItem() {
        List<LineItem> singleItem = List.of(laptopQty2());

        RevenueReport report = singleItem.stream()
                .collect(new CustomRevenueCollector());

        assertEquals(1L, report.getItemCount());
        assertEquals(1999.98, report.getTotalRevenue(), 0.001);
        assertEquals(1999.98, report.getMaxSingleItemRevenue(), 0.001);
    }

    // ===== Exercise 2.3 Part 2: Parallel Stream Verification =====

    @Test
    void parallelStream_shouldProduceSameResultsAsSequential() {
        int n = 3;
        ProductAnalytics analytics = new ProductAnalytics();

        List<Map.Entry<Product, Double>> sequential = 
                analytics.topNProductsByRevenue(orders, n);
        List<Map.Entry<Product, Double>> parallel = 
                analytics.topNProductsByRevenueParallel(orders, n);

        assertEquals(sequential.size(), parallel.size());
        
        for (int i = 0; i < sequential.size(); i++) {
            assertEquals(sequential.get(i).getKey().getId(), 
                        parallel.get(i).getKey().getId());
            assertEquals(sequential.get(i).getValue(), 
                        parallel.get(i).getValue(), 0.001);
        }
    }

    @Test
    void parallelStream_shouldHandleEdgeCaseEmptyOrders() {
        List<Order> emptyOrders = List.of();
        int n = 5;
        ProductAnalytics analytics = new ProductAnalytics();

        List<Map.Entry<Product, Double>> sequential = 
                analytics.topNProductsByRevenue(emptyOrders, n);
        List<Map.Entry<Product, Double>> parallel = 
                analytics.topNProductsByRevenueParallel(emptyOrders, n);

        assertTrue(sequential.isEmpty());
        assertTrue(parallel.isEmpty());
    }

    // ===== Exercise 2.3 Part 3: Benchmark =====

    @Test
    void benchmarkSequentialVsParallelOnLargeDataset() {
        List<Order> largeOrders = generateLargeOrderDataset(100_000);
        int n = 10;
        ProductAnalytics analytics = new ProductAnalytics();

        // Warm up JVM
        for (int i = 0; i < 3; i++) {
            analytics.topNProductsByRevenue(largeOrders, n);
            analytics.topNProductsByRevenueParallel(largeOrders, n);
        }

        // Benchmark sequential
        long startSeq = System.nanoTime();
        List<Map.Entry<Product, Double>> sequentialResult = 
                analytics.topNProductsByRevenue(largeOrders, n);
        long seqTime = System.nanoTime() - startSeq;

        // Benchmark parallel
        long startPar = System.nanoTime();
        List<Map.Entry<Product, Double>> parallelResult = 
                analytics.topNProductsByRevenueParallel(largeOrders, n);
        long parTime = System.nanoTime() - startPar;

        // Verify results are identical
        assertEquals(sequentialResult.size(), parallelResult.size());
        for (int i = 0; i < sequentialResult.size(); i++) {
            assertEquals(sequentialResult.get(i).getKey().getId(), 
                        parallelResult.get(i).getKey().getId());
        }

        // Print benchmark results
        System.out.println("========== BENCHMARK RESULTS ==========");
        System.out.println("Dataset size: 100,000 orders");
        System.out.println("Sequential time: " + seqTime / 1_000_000 + " ms");
        System.out.println("Parallel time:   " + parTime / 1_000_000 + " ms");
        System.out.println("Speedup:         " + String.format("%.2f", (double) seqTime / parTime) + "x");
        
        if (parTime < seqTime) {
            System.out.println("Parallel was faster on this dataset");
        } else {
            System.out.println("Sequential was faster on this dataset");
        }
        System.out.println("========================================");
    }

    @Test
    void benchmarkSmallDataset_sequentialVsParallel() {
        int n = 5;
        ProductAnalytics analytics = new ProductAnalytics();

        // Warm up
        for (int i = 0; i < 3; i++) {
            analytics.topNProductsByRevenue(orders, n);
            analytics.topNProductsByRevenueParallel(orders, n);
        }

        // Benchmark sequential
        long startSeq = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            analytics.topNProductsByRevenue(orders, n);
        }
        long seqTime = System.nanoTime() - startSeq;

        // Benchmark parallel
        long startPar = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            analytics.topNProductsByRevenueParallel(orders, n);
        }
        long parTime = System.nanoTime() - startPar;

        System.out.println("========== SMALL DATASET BENCHMARK ==========");
        System.out.println("Dataset size: 3 orders (9 line items), run 1000 times");
        System.out.println("Sequential time: " + seqTime / 1_000_000 + " ms");
        System.out.println("Parallel time:   " + parTime / 1_000_000 + " ms");
        System.out.println("==============================================");
    }

    // ===== Helper Method =====

    private List<Order> generateLargeOrderDataset(int numberOfOrders) {
        List<Order> orders = new ArrayList<>();
        List<Product> products = List.of(
                new Product("P001", "Laptop", "Electronics", 999.99),
                new Product("P002", "Smartphone", "Electronics", 699.99),
                new Product("P003", "Java Book", "Books", 49.99),
                new Product("P004", "Headphones", "Electronics", 89.99),
                new Product("P005", "T-Shirt", "Clothing", 19.99),
                new Product("P006", "Coffee Mug", "Kitchen", 14.99),
                new Product("P007", "Backpack", "Accessories", 59.99),
                new Product("P008", "Sunglasses", "Accessories", 129.99),
                new Product("P009", "Notebook", "Office", 9.99),
                new Product("P010", "Water Bottle", "Kitchen", 24.99)
        );

        for (int i = 0; i < numberOfOrders; i++) {
            List<LineItem> items = new ArrayList<>();
            int numItems = (int) (Math.random() * 5) + 1;
            for (int j = 0; j < numItems; j++) {
                Product product = products.get((int) (Math.random() * products.size()));
                int quantity = (int) (Math.random() * 10) + 1;
                items.add(new LineItem(product, quantity));
            }
            boolean delivered = Math.random() > 0.5;
            orders.add(new Order("ORD-" + i, items, delivered));
        }
        return orders;
    }
}