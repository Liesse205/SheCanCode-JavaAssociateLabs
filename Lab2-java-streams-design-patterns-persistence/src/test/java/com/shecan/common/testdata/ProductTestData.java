package com.shecan.common.testdata;

import com.shecan.lab21.scenario1_ecommerce_analytics.model.Product;

import java.util.Arrays;
import java.util.List;

public class ProductTestData {
    
    public static Product laptop() {
        return new Product("P001", "Laptop", "Electronics", 999.99);
    }
    
    public static Product phone() {
        return new Product("P002", "Smartphone", "Electronics", 699.99);
    }
    
    public static Product book() {
        return new Product("P003", "Java Book", "Books", 49.99);
    }
    
    public static Product headphones() {
        return new Product("P004", "Headphones", "Electronics", 89.99);
    }
    
    public static Product shirt() {
        return new Product("P005", "T-Shirt", "Clothing", 19.99);
    }
    
    public static List<Product> allProducts() {
        return Arrays.asList(laptop(), phone(), book(), headphones(), shirt());
    }
}