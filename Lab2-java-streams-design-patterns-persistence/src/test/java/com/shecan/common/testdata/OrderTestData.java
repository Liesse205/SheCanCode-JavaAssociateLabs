package com.shecan.common.testdata;

import com.shecan.lab21.scenario1_ecommerce_analytics.model.LineItem;
import com.shecan.lab21.scenario1_ecommerce_analytics.model.Order;

import java.util.Arrays;
import java.util.List;

import static com.shecan.common.testdata.ProductTestData.*;

public class OrderTestData {

    public static Order order1() {
        List<LineItem> items = Arrays.asList(
                new LineItem(laptop(), 2),      // Revenue: 1999.98
                new LineItem(phone(), 10),      // Revenue: 6999.90 (quantity > 5)
                new LineItem(book(), 3)         // Revenue: 149.97
        );
        return new Order("ORD-001", items, true);
    }

    public static Order order2() {
        List<LineItem> items = Arrays.asList(
                new LineItem(phone(), 1),       // Revenue: 699.99
                new LineItem(headphones(), 7),  // Revenue: 629.93 (quantity > 5)
                new LineItem(shirt(), 12)       // Revenue: 239.88 (quantity > 5)
        );
        return new Order("ORD-002", items, false);
    }

    public static Order order3() {
        List<LineItem> items = Arrays.asList(
                new LineItem(laptop(), 3),      // Revenue: 2999.97
                new LineItem(book(), 8),        // Revenue: 399.92 (quantity > 5)
                new LineItem(shirt(), 5)        // Revenue: 99.95
        );
        return new Order("ORD-003", items, true);
    }

    public static List<Order> sampleOrders() {
        return Arrays.asList(order1(), order2(), order3());
    }
}