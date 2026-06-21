package com.shecan.lab21.scenario1_ecommerce_analytics.model;

public class LineItem {
    private final Product product;
    private final int quantity;

    public LineItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public String getCategory() { return product.getCategory(); }
    
    public double getRevenue() {
        return product.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return "LineItem{product=" + product.getName() + ", quantity=" + quantity + ", revenue=" + getRevenue() + "}";
    }
}