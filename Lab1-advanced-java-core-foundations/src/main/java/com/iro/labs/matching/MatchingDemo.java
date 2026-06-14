package com.iro.labs.matching;

import com.iro.labs.matching.engine.OrderBook;
import com.iro.labs.matching.executor.BuyerTask;
import com.iro.labs.matching.executor.OrderMatchingEngine;
import com.iro.labs.matching.executor.SellerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

public class MatchingDemo {
    private static final Logger log = LoggerFactory.getLogger(MatchingDemo.class);
    
    public static void main(String[] args) {
        System.out.println("=== Lab 1.3: Real-Time Order Matching Engine ===\n");
        
        exercise17Demo();
        exercise18Demo();
    }
    
    private static void exercise17Demo() {
        System.out.println("--- Exercise 1.7: Thread-Safe Order Book ---");
        
        OrderBook orderBook = new OrderBook();
        int ordersPerThread = 10;
        
        List<Thread> buyers = new ArrayList<>();
        List<Thread> sellers = new ArrayList<>();
        
        for (int i = 0; i < 10; i++) {
            Thread buyer = new Thread(new BuyerTask(orderBook, "AAPL", ordersPerThread));
            Thread seller = new Thread(new SellerTask(orderBook, "AAPL", ordersPerThread));
            buyers.add(buyer);
            sellers.add(seller);
            buyer.start();
            seller.start();
        }
        
        for (Thread buyer : buyers) {
            try {
                buyer.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        for (Thread seller : sellers) {
            try {
                seller.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        System.out.println("Total orders added: " + orderBook.getTotalOrdersAdded());
        System.out.println("Buy orders remaining: " + orderBook.getBuyOrderCount());
        System.out.println("Sell orders remaining: " + orderBook.getSellOrderCount());
        
        List<com.iro.labs.matching.model.MatchResult> matches = orderBook.matchOrders();
        
        System.out.println("New matches found: " + matches.size());
        System.out.println("Total matches all time: " + orderBook.getTotalMatchesCount());
        
        if (!matches.isEmpty()) {
            System.out.println("Sample match: " + matches.get(0));
        }
        System.out.println();
    }
    
    private static void exercise18Demo() {
        System.out.println("--- Exercise 1.8: ExecutorService Orchestration ---");
        
        OrderBook orderBook = new OrderBook();
        OrderMatchingEngine engine = new OrderMatchingEngine(orderBook);
        
        int ordersPerTask = 5;
        List<Runnable> orderTasks = new ArrayList<>();
        
        for (int i = 0; i < 10; i++) {
            orderTasks.add(new BuyerTask(orderBook, "AAPL", ordersPerTask));
            orderTasks.add(new SellerTask(orderBook, "AAPL", ordersPerTask));
        }
        
        try {
            engine.submitOrders(orderTasks);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Order submission interrupted");
        }
        
        System.out.println("Total orders added: " + orderBook.getTotalOrdersAdded());
        System.out.println("Buy orders in book: " + orderBook.getBuyOrderCount());
        System.out.println("Sell orders in book: " + orderBook.getSellOrderCount());
        
        double throughput = engine.measureThroughput(5);
        System.out.printf("Throughput: %.2f matches per second (averaged over 5 runs after warm-up)%n", throughput);
        
        System.out.println("\nFinal match statistics:");
        System.out.println("Total matches executed: " + orderBook.getTotalMatchesCount());
        
        engine.shutdown();
        System.out.println();
    }
}