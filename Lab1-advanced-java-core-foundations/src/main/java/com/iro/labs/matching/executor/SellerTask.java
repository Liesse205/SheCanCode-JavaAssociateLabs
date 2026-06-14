package com.iro.labs.matching.executor;

import com.iro.labs.matching.engine.OrderBook;
import com.iro.labs.matching.model.Order;
import com.iro.labs.matching.model.OrderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.Random;

public class SellerTask implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(SellerTask.class);
    private final OrderBook orderBook;
    private final String productId;
    private final Random random;
    private final int ordersPerThread;
    
    public SellerTask(OrderBook orderBook, String productId, int ordersPerThread) {
        this.orderBook = orderBook;
        this.productId = productId;
        this.random = new Random();
        this.ordersPerThread = ordersPerThread;
    }
    
    @Override
    public void run() {
        for (int i = 0; i < ordersPerThread; i++) {
            int quantity = random.nextInt(90) + 10;
            double priceValue = 90 + random.nextDouble() * 20;
            BigDecimal price = BigDecimal.valueOf(priceValue).setScale(2, BigDecimal.ROUND_HALF_UP);
            
            Order sellOrder = new Order(productId, OrderType.SELL, quantity, price);
            orderBook.addOrder(sellOrder);
            
            log.debug("Seller created order: {} units at {}", quantity, price);
        }
    }
}