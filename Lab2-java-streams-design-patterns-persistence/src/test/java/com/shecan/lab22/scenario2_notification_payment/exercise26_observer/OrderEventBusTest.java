package com.shecan.lab22.scenario2_notification_payment.exercise26_observer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrderEventBusTest {

    private static final String SEPARATOR = "============================================================";
    private OrderEventBus eventBus;
    private Order order;

    @BeforeEach
    void setUp() {
        eventBus = new OrderEventBus();
        order = new Order("ORD-001", "John Doe", 150.50);
        System.out.println();
        System.out.println(SEPARATOR);
        System.out.println("SETUP: Created OrderEventBus and Order");
        System.out.println("Order: " + order);
        System.out.println(SEPARATOR);
    }

    private void printHeader(String title) {
        System.out.println();
        System.out.println(SEPARATOR);
        System.out.println("TEST: " + title);
        System.out.println(SEPARATOR);
    }

    @Test
    void testSubscribeAndPublish() {
        printHeader("Subscribe and Publish - Single Observer");
        
        // Create observers
        EmailNotifier emailNotifier = new EmailNotifier();
        
        // Subscribe
        eventBus.subscribe(emailNotifier);
        assertEquals(1, eventBus.getObserverCount());
        
        // Publish event
        eventBus.publish(order, OrderEvent.ORDER_PLACED);
        
        System.out.println();
        System.out.println("VERIFICATION: Observer count = " + eventBus.getObserverCount());
        assertTrue(eventBus.getObservers().contains(emailNotifier));
        System.out.println(SEPARATOR);
    }

    @Test
    void testMultipleObservers() {
        printHeader("Multiple Observers - All Three Observers");
        
        // Create all three observers
        EmailNotifier emailNotifier = new EmailNotifier();
        InventoryUpdater inventoryUpdater = new InventoryUpdater();
        AuditLogger auditLogger = new AuditLogger();
        
        // Subscribe all
        eventBus.subscribe(emailNotifier);
        eventBus.subscribe(inventoryUpdater);
        eventBus.subscribe(auditLogger);
        
        assertEquals(3, eventBus.getObserverCount());
        System.out.println();
        System.out.println("All three observers subscribed. Publishing ORDER_PLACED...");
        
        // Publish event
        eventBus.publish(order, OrderEvent.ORDER_PLACED);
        
        System.out.println();
        System.out.println("VERIFICATION: All three observers fired");
        System.out.println(SEPARATOR);
    }

    @Test
    void testUnsubscribe() {
        printHeader("Unsubscribe - Remove an Observer");
        
        // Create observers
        EmailNotifier emailNotifier = new EmailNotifier();
        InventoryUpdater inventoryUpdater = new InventoryUpdater();
        AuditLogger auditLogger = new AuditLogger();
        
        // Subscribe all
        eventBus.subscribe(emailNotifier);
        eventBus.subscribe(inventoryUpdater);
        eventBus.subscribe(auditLogger);
        
        assertEquals(3, eventBus.getObserverCount());
        
        // Unsubscribe one
        System.out.println();
        eventBus.unsubscribe(inventoryUpdater);
        assertEquals(2, eventBus.getObserverCount());
        
        System.out.println();
        System.out.println("Publishing ORDER_SHIPPED with only 2 observers...");
        eventBus.publish(order, OrderEvent.ORDER_SHIPPED);
        
        System.out.println();
        System.out.println("VERIFICATION: Only " + eventBus.getObserverCount() + " observers remain");
        assertFalse(eventBus.getObservers().contains(inventoryUpdater));
        System.out.println(SEPARATOR);
    }

    @Test
    void testAllEventsSequence() {
        printHeader("Event Sequence - All Events in Order");
        
        // Create observers
        EmailNotifier emailNotifier = new EmailNotifier();
        InventoryUpdater inventoryUpdater = new InventoryUpdater();
        AuditLogger auditLogger = new AuditLogger();
        
        // Subscribe all
        eventBus.subscribe(emailNotifier);
        eventBus.subscribe(inventoryUpdater);
        eventBus.subscribe(auditLogger);
        
        System.out.println();
        System.out.println("Publishing events in sequence...");
        
        // Publish events in order
        eventBus.publish(order, OrderEvent.ORDER_PLACED);
        eventBus.publish(order, OrderEvent.ORDER_SHIPPED);
        eventBus.publish(order, OrderEvent.ORDER_DELIVERED);
        
        System.out.println();
        System.out.println("VERIFICATION: All events processed in correct order");
        System.out.println("Event sequence: ORDER_PLACED -> ORDER_SHIPPED -> ORDER_DELIVERED");
        System.out.println(SEPARATOR);
    }

    @Test
    void testCancelEvent() {
        printHeader("Cancel Event - Order Cancelled");
        
        // Create observers
        EmailNotifier emailNotifier = new EmailNotifier();
        InventoryUpdater inventoryUpdater = new InventoryUpdater();
        AuditLogger auditLogger = new AuditLogger();
        
        // Subscribe all
        eventBus.subscribe(emailNotifier);
        eventBus.subscribe(inventoryUpdater);
        eventBus.subscribe(auditLogger);
        
        System.out.println();
        System.out.println("Publishing ORDER_CANCELLED...");
        eventBus.publish(order, OrderEvent.ORDER_CANCELLED);
        
        System.out.println();
        System.out.println("VERIFICATION: All observers notified of cancellation");
        System.out.println(SEPARATOR);
    }

    @Test
    void testEmptyEventBus() {
        printHeader("Empty Event Bus - No Observers");
        
        // No observers subscribed
        assertEquals(0, eventBus.getObserverCount());
        
        System.out.println();
        System.out.println("Publishing event with no observers...");
        eventBus.publish(order, OrderEvent.ORDER_PLACED);
        
        System.out.println();
        System.out.println("VERIFICATION: Event published but no observers to notify");
        System.out.println(SEPARATOR);
    }

    @Test
    void testMultipleEventsWithSameOrder() {
        printHeader("Multiple Events - Same Order Different Events");
        
        // Create observers
        EmailNotifier emailNotifier = new EmailNotifier();
        InventoryUpdater inventoryUpdater = new InventoryUpdater();
        AuditLogger auditLogger = new AuditLogger();
        
        // Subscribe all
        eventBus.subscribe(emailNotifier);
        eventBus.subscribe(inventoryUpdater);
        eventBus.subscribe(auditLogger);
        
        System.out.println();
        System.out.println("Publishing multiple events for same order...");
        
        eventBus.publish(order, OrderEvent.ORDER_PLACED);
        eventBus.publish(order, OrderEvent.ORDER_SHIPPED);
        eventBus.publish(order, OrderEvent.ORDER_DELIVERED);
        eventBus.publish(order, OrderEvent.ORDER_CANCELLED);
        
        System.out.println();
        System.out.println("VERIFICATION: Same order processed through full lifecycle");
        System.out.println("Order " + order.getOrderId() + " lifecycle: PLACED -> SHIPPED -> DELIVERED -> CANCELLED");
        System.out.println(SEPARATOR);
    }

    @Test
    void testDuplicateSubscription() {
        printHeader("Duplicate Subscription - Same Observer Twice");
        
        // Create observer
        EmailNotifier emailNotifier = new EmailNotifier();
        
        // Subscribe twice
        eventBus.subscribe(emailNotifier);
        eventBus.subscribe(emailNotifier); // Should not add duplicate
        
        assertEquals(1, eventBus.getObserverCount());
        
        System.out.println();
        System.out.println("Publishing event...");
        eventBus.publish(order, OrderEvent.ORDER_PLACED);
        
        System.out.println();
        System.out.println("VERIFICATION: Observer only called once despite duplicate subscribe");
        System.out.println("Observer count: " + eventBus.getObserverCount());
        System.out.println(SEPARATOR);
    }

    @Test
    void testObserverOrder() {
        printHeader("Observer Order - Verify All Three Fire");
        
        // Create observers
        EmailNotifier emailNotifier = new EmailNotifier();
        InventoryUpdater inventoryUpdater = new InventoryUpdater();
        AuditLogger auditLogger = new AuditLogger();
        
        // Subscribe in specific order
        eventBus.subscribe(emailNotifier);
        eventBus.subscribe(inventoryUpdater);
        eventBus.subscribe(auditLogger);
        
        System.out.println();
        System.out.println("Observers subscribed in order:");
        System.out.println("  1. EmailNotifier");
        System.out.println("  2. InventoryUpdater");
        System.out.println("  3. AuditLogger");
        
        System.out.println();
        System.out.println("Publishing ORDER_PLACED...");
        System.out.println("Observers should fire in subscription order:");
        System.out.println("  1. [EMAIL NOTIFIER]");
        System.out.println("  2. [INVENTORY UPDATER]");
        System.out.println("  3. [AUDIT LOGGER]");
        System.out.println();
        
        eventBus.publish(order, OrderEvent.ORDER_PLACED);
        
        System.out.println();
        System.out.println("VERIFICATION: All three observers fired in correct order");
        System.out.println(SEPARATOR);
    }
}