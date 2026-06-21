package com.shecan.common.testdata;

import com.shecan.lab21.scenario1_ecommerce_analytics.model.Order;

import java.util.Arrays;
import java.util.List;

import static com.shecan.common.testdata.LineItemTestData.*;

public class OrderTestData {

    public static Order order1() {
        return new Order("ORD-001", order1LineItems(), true);
    }

    public static Order order2() {
        return new Order("ORD-002", order2LineItems(), false);
    }

    public static Order order3() {
        return new Order("ORD-003", order3LineItems(), true);
    }

    public static List<Order> sampleOrders() {
        return Arrays.asList(order1(), order2(), order3());
    }
}