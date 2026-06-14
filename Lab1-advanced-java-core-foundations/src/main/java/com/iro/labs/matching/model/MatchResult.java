package com.iro.labs.matching.model;

import java.math.BigDecimal;

public class MatchResult {
    private final Order buyOrder;
    private final Order sellOrder;
    private final int matchedQuantity;
    private final BigDecimal matchPrice;
    
    public MatchResult(Order buyOrder, Order sellOrder, int matchedQuantity, BigDecimal matchPrice) {
        this.buyOrder = buyOrder;
        this.sellOrder = sellOrder;
        this.matchedQuantity = matchedQuantity;
        this.matchPrice = matchPrice;
    }
    
    public Order getBuyOrder() {
        return buyOrder;
    }
    
    public Order getSellOrder() {
        return sellOrder;
    }
    
    public int getMatchedQuantity() {
        return matchedQuantity;
    }
    
    public BigDecimal getMatchPrice() {
        return matchPrice;
    }
    
    @Override
    public String toString() {
        return String.format("Match: %d units at %s (Buy: %s, Sell: %s)",
                            matchedQuantity, matchPrice, 
                            buyOrder.getId().substring(0, 8),
                            sellOrder.getId().substring(0, 8));
    }
}