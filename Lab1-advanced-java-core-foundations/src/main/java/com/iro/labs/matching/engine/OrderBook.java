package com.iro.labs.matching.engine;

import com.iro.labs.matching.model.Order;
import com.iro.labs.matching.model.OrderType;
import com.iro.labs.matching.model.MatchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class OrderBook {
    private static final Logger log = LoggerFactory.getLogger(OrderBook.class);
    
    private final Queue<Order> buyOrders;
    private final Queue<Order> sellOrders;
    private final ReentrantLock lock;
    private final List<MatchResult> matchResults;
    private int totalOrdersAdded;
    
    public OrderBook() {
        this.buyOrders = new LinkedList<>();
        this.sellOrders = new LinkedList<>();
        this.lock = new ReentrantLock();
        this.matchResults = new ArrayList<>();
        this.totalOrdersAdded = 0;
    }
    
    public void addOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        
        lock.lock();
        try {
            if (order.getType() == OrderType.BUY) {
                buyOrders.add(order);
                log.debug("Added BUY order: {}", order.getId().substring(0, 8));
            } else {
                sellOrders.add(order);
                log.debug("Added SELL order: {}", order.getId().substring(0, 8));
            }
            totalOrdersAdded++;
        } finally {
            lock.unlock();
        }
    }
    
    public List<MatchResult> matchOrders() {
        lock.lock();
        try {
            List<MatchResult> newMatches = new ArrayList<>();
            
            while (!buyOrders.isEmpty() && !sellOrders.isEmpty()) {
                Order buyOrder = buyOrders.peek();
                Order sellOrder = sellOrders.peek();
                
                if (buyOrder.getPrice().compareTo(sellOrder.getPrice()) >= 0) {
                    int matchedQty = Math.min(buyOrder.getRemainingQuantity(), sellOrder.getRemainingQuantity());
                    BigDecimal matchPrice = sellOrder.getPrice();
                    
                    MatchResult match = new MatchResult(buyOrder, sellOrder, matchedQty, matchPrice);
                    newMatches.add(match);
                    matchResults.add(match);
                    
                    log.info("Match found: {} units at price {}", matchedQty, matchPrice);
                    
                    buyOrder.reduceQuantity(matchedQty);
                    sellOrder.reduceQuantity(matchedQty);
                    
                    if (buyOrder.isFilled()) {
                        buyOrders.poll();
                    }
                    
                    if (sellOrder.isFilled()) {
                        sellOrders.poll();
                    }
                } else {
                    break;
                }
            }
            
            return newMatches;
        } finally {
            lock.unlock();
        }
    }
    
    public int getBuyOrderCount() {
        lock.lock();
        try {
            return buyOrders.size();
        } finally {
            lock.unlock();
        }
    }
    
    public int getSellOrderCount() {
        lock.lock();
        try {
            return sellOrders.size();
        } finally {
            lock.unlock();
        }
    }
    
    public List<MatchResult> getAllMatches() {
        lock.lock();
        try {
            return new ArrayList<>(matchResults);
        } finally {
            lock.unlock();
        }
    }
    
    public int getTotalMatchesCount() {
        lock.lock();
        try {
            return matchResults.size();
        } finally {
            lock.unlock();
        }
    }
    
    public int getTotalOrdersAdded() {
        lock.lock();
        try {
            return totalOrdersAdded;
        } finally {
            lock.unlock();
        }
    }
}