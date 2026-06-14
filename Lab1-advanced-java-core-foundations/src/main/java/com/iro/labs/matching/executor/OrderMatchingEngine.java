package com.iro.labs.matching.executor;

import com.iro.labs.matching.engine.OrderBook;
import com.iro.labs.matching.model.MatchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class OrderMatchingEngine {
    private static final Logger log = LoggerFactory.getLogger(OrderMatchingEngine.class);
    private final OrderBook orderBook;
    private final ExecutorService executorService;
    
    public OrderMatchingEngine(OrderBook orderBook) {
        this.orderBook = orderBook;
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        this.executorService = Executors.newFixedThreadPool(availableProcessors);
        log.info("Created thread pool with {} threads", availableProcessors);
    }
    
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    public int submitMatchingTask() throws ExecutionException, InterruptedException {
        MatchingTask task = new MatchingTask(orderBook);
        Future<Integer> future = executorService.submit(task);
        
        try {
            return future.get();
        } catch (ExecutionException e) {
            log.error("Matching task failed", e);
            throw e;
        }
    }
    
    public void submitOrders(List<Runnable> tasks) throws InterruptedException {
        List<Future<?>> futures = new ArrayList<>();
        
        for (Runnable task : tasks) {
            futures.add(executorService.submit(task));
        }
        
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (ExecutionException e) {
                log.error("Order submission failed", e.getCause());
            }
        }
    }
    
    public double measureThroughput(int matchingRuns) {
        log.info("Starting throughput measurement with warm-up");
        
        int warmupRuns = 1;
        
        for (int i = 0; i < warmupRuns; i++) {
            try {
                submitMatchingTask();
            } catch (Exception e) {
                log.warn("Warm-up run failed", e);
            }
        }
        
        log.info("Warm-up complete. Running {} measured runs", matchingRuns);
        
        long startTime = System.nanoTime();
        
        for (int i = 0; i < matchingRuns; i++) {
            try {
                submitMatchingTask();
            } catch (Exception e) {
                log.error("Matching failed on run {}", i, e);
            }
        }
        
        long endTime = System.nanoTime();
        double durationSeconds = (endTime - startTime) / 1_000_000_000.0;
        
        double throughput = matchingRuns / durationSeconds;
        log.info("Throughput: {:.2f} matches per second", throughput);
        
        return throughput;
    }
    
    public OrderBook getOrderBook() {
        return orderBook;
    }
}