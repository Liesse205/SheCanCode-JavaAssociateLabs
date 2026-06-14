package com.iro.labs.matching.executor;

import com.iro.labs.matching.engine.OrderBook;
import com.iro.labs.matching.model.MatchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.concurrent.Callable;

public class MatchingTask implements Callable<Integer> {
    private static final Logger log = LoggerFactory.getLogger(MatchingTask.class);
    private final OrderBook orderBook;
    
    public MatchingTask(OrderBook orderBook) {
        this.orderBook = orderBook;
    }
    
    @Override
    public Integer call() {
        List<MatchResult> matches = orderBook.matchOrders();
        log.debug("Matching completed. Found {} matches", matches.size());
        return matches.size();
    }
}