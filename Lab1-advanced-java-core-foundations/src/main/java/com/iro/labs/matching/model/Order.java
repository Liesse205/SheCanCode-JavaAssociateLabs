package com.iro.labs.matching.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Order {
    private final String id;
    private final String productId;
    private final OrderType type;
    private int remainingQuantity;
    private final int originalQuantity;
    private final BigDecimal price;
    private final long timestamp;
    
    public Order(String productId, OrderType type, int quantity, BigDecimal price) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        
        this.id = UUID.randomUUID().toString();
        this.productId = productId;
        this.type = type;
        this.remainingQuantity = quantity;
        this.originalQuantity = quantity;
        this.price = price;
        this.timestamp = System.currentTimeMillis();
    }
    
    public String getId() {
        return id;
    }
    
    public String getProductId() {
        return productId;
    }
    
    public OrderType getType() {
        return type;
    }
    
    public int getRemainingQuantity() {
        return remainingQuantity;
    }
    
    public int getOriginalQuantity() {
        return originalQuantity;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public boolean isFilled() {
        return remainingQuantity == 0;
    }
    
    public void reduceQuantity(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Reduction amount must be positive");
        }
        if (amount > remainingQuantity) {
            throw new IllegalArgumentException("Cannot reduce more than remaining quantity");
        }
        this.remainingQuantity -= amount;
    }
    
    @Override
    public String toString() {
        return String.format("Order{id='%s', product='%s', type=%s, qty=%d/%d, price=%s}",
                            id.substring(0, 8), productId, type, remainingQuantity, originalQuantity, price);
    }
}