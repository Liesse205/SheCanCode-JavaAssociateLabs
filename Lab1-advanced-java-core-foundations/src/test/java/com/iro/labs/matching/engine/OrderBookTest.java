package com.iro.labs.matching.engine;

import com.iro.labs.matching.model.Order;
import com.iro.labs.matching.model.OrderType;
import com.iro.labs.matching.model.MatchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class OrderBookTest {
    private OrderBook orderBook;
    
    @BeforeEach
    void setUp() {
        orderBook = new OrderBook();
    }
    
    @Test
    void testAddOrder() {
        Order buyOrder = new Order("AAPL", OrderType.BUY, 100, new BigDecimal("150.00"));
        orderBook.addOrder(buyOrder);
        
        assertEquals(1, orderBook.getBuyOrderCount());
        assertEquals(0, orderBook.getSellOrderCount());
    }
    
    @Test
    void testMatchOrders() {
        Order buyOrder = new Order("AAPL", OrderType.BUY, 100, new BigDecimal("150.00"));
        Order sellOrder = new Order("AAPL", OrderType.SELL, 100, new BigDecimal("140.00"));
        
        orderBook.addOrder(buyOrder);
        orderBook.addOrder(sellOrder);
        
        List<MatchResult> matches = orderBook.matchOrders();
        
        assertEquals(1, matches.size());
        assertEquals(100, matches.get(0).getMatchedQuantity());
        assertEquals(new BigDecimal("140.00"), matches.get(0).getMatchPrice());
    }
    
    @Test
    void testNoMatchWhenBuyPriceLowerThanSellPrice() {
        Order buyOrder = new Order("AAPL", OrderType.BUY, 100, new BigDecimal("130.00"));
        Order sellOrder = new Order("AAPL", OrderType.SELL, 100, new BigDecimal("150.00"));
        
        orderBook.addOrder(buyOrder);
        orderBook.addOrder(sellOrder);
        
        List<MatchResult> matches = orderBook.matchOrders();
        
        assertEquals(0, matches.size());
    }
    
    @Test
    void testPartialMatch() {
        Order buyOrder = new Order("AAPL", OrderType.BUY, 50, new BigDecimal("150.00"));
        Order sellOrder = new Order("AAPL", OrderType.SELL, 100, new BigDecimal("140.00"));
        
        orderBook.addOrder(buyOrder);
        orderBook.addOrder(sellOrder);
        
        List<MatchResult> matches = orderBook.matchOrders();
        
        assertEquals(1, matches.size());
        assertEquals(50, matches.get(0).getMatchedQuantity());
        
        assertEquals(1, orderBook.getSellOrderCount());
    }
    
    @Test
    void testOrderQuantityReduction() {
        Order buyOrder = new Order("AAPL", OrderType.BUY, 50, new BigDecimal("150.00"));
        Order sellOrder = new Order("AAPL", OrderType.SELL, 100, new BigDecimal("140.00"));
        
        orderBook.addOrder(buyOrder);
        orderBook.addOrder(sellOrder);
        
        orderBook.matchOrders();
        
        assertTrue(buyOrder.isFilled());
        assertEquals(50, sellOrder.getRemainingQuantity());
    }
}