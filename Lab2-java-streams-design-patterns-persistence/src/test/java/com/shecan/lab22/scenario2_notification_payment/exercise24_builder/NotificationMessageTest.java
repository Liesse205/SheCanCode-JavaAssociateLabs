package com.shecan.lab22.scenario2_notification_payment.exercise24_builder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationMessageTest {

    // ===== Helper method to print section headers =====
    private static void printHeader(String title) {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("TEST: " + title);
        System.out.println("============================================================");
    }

    private static void printResult(String status, String details) {
        System.out.println("RESULT: " + status);
        System.out.println("DETAILS: " + details);
        System.out.println("------------------------------------------------------------");
    }

    @Test
    void testValidNotificationConstruction() {
        printHeader("Valid Notification Construction");
        
        NotificationMessage notification = new NotificationMessage.Builder()
                .to("user@example.com")
                .subject("Welcome to the System")
                .body("Hello! Your account has been created successfully.")
                .priority(NotificationMessage.Priority.HIGH)
                .attach("welcome.pdf")
                .attach("setup-guide.pdf")
                .build();

        assertEquals("user@example.com", notification.getRecipient());
        assertEquals("Welcome to the System", notification.getSubject());
        assertEquals("Hello! Your account has been created successfully.", notification.getBody());
        assertEquals(NotificationMessage.Priority.HIGH, notification.getPriority());
        assertEquals(2, notification.getAttachments().size());
        assertTrue(notification.getAttachments().contains("welcome.pdf"));
        assertTrue(notification.getAttachments().contains("setup-guide.pdf"));

        printResult("PASSED", "Notification built successfully");
        System.out.println(notification);
        System.out.println("============================================================");
    }

    @Test
    void testValidConstructionWithDefaults() {
        printHeader("Valid Construction With Defaults");
        
        NotificationMessage notification = new NotificationMessage.Builder()
                .to("admin@system.com")
                .body("System update completed.")
                .build();

        assertEquals("admin@system.com", notification.getRecipient());
        assertEquals("System update completed.", notification.getBody());
        assertEquals(NotificationMessage.Priority.MEDIUM, notification.getPriority());
        assertNull(notification.getSubject());
        assertTrue(notification.getAttachments().isEmpty());

        printResult("PASSED", "Notification built with defaults");
        System.out.println(notification);
        System.out.println("============================================================");
    }

    @Test
    void testBuilderThrowsExceptionWhenRecipientIsNull() {
        printHeader("Validation - Null Recipient");
        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            new NotificationMessage.Builder()
                    .subject("Important Message")
                    .body("This should fail because recipient is null")
                    .build();
        });

        assertEquals("Recipient cannot be null or empty", exception.getMessage());
        printResult("PASSED", "Correctly threw exception: " + exception.getMessage());
        System.out.println("============================================================");
    }

    @Test
    void testBuilderThrowsExceptionWhenRecipientIsEmpty() {
        printHeader("Validation - Empty Recipient");
        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            new NotificationMessage.Builder()
                    .to("   ")
                    .subject("Important Message")
                    .body("This should fail because recipient is empty")
                    .build();
        });

        assertEquals("Recipient cannot be null or empty", exception.getMessage());
        printResult("PASSED", "Correctly threw exception: " + exception.getMessage());
        System.out.println("============================================================");
    }

    @Test
    void testBuilderThrowsExceptionWhenBodyIsNull() {
        printHeader("Validation - Null Body");
        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            new NotificationMessage.Builder()
                    .to("user@example.com")
                    .subject("Important Message")
                    .build();
        });

        assertEquals("Body cannot be null or empty", exception.getMessage());
        printResult("PASSED", "Correctly threw exception: " + exception.getMessage());
        System.out.println("============================================================");
    }

    @Test
    void testBuilderThrowsExceptionWhenBodyIsEmpty() {
        printHeader("Validation - Empty Body");
        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            new NotificationMessage.Builder()
                    .to("user@example.com")
                    .subject("Important Message")
                    .body("   ")
                    .build();
        });

        assertEquals("Body cannot be null or empty", exception.getMessage());
        printResult("PASSED", "Correctly threw exception: " + exception.getMessage());
        System.out.println("============================================================");
    }

    @Test
    void testMethodChainingFluency() {
        printHeader("Method Chaining Fluency");
        System.out.println("Demonstrating the fluent builder interface:");
        System.out.println("  new NotificationMessage.Builder()");
        System.out.println("      .to(\"user@example.com\")");
        System.out.println("      .subject(\"Test\")");
        System.out.println("      .body(\"Message\")");
        System.out.println("      .priority(Priority.HIGH)");
        System.out.println("      .attach(\"file1.txt\")");
        System.out.println("      .attach(\"file2.txt\")");
        System.out.println("      .build();");
        System.out.println();
        
        NotificationMessage notification = new NotificationMessage.Builder()
                .to("demo@test.com")
                .subject("Fluent Builder Demo")
                .body("This demonstrates method chaining!")
                .priority(NotificationMessage.Priority.LOW)
                .attach("demo1.txt")
                .attach("demo2.txt")
                .build();

        assertEquals("demo@test.com", notification.getRecipient());
        assertEquals("Fluent Builder Demo", notification.getSubject());
        assertEquals("This demonstrates method chaining!", notification.getBody());
        assertEquals(NotificationMessage.Priority.LOW, notification.getPriority());
        assertEquals(2, notification.getAttachments().size());

        printResult("PASSED", "Fluent builder works correctly");
        System.out.println(notification);
        System.out.println("============================================================");
    }

    // ===== Run this as a main method in IntelliJ to see all output =====
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("RUNNING BUILDER PATTERN TESTS WITH VISIBLE OUTPUT");
        System.out.println("============================================================");

        // Test 1: Valid construction
        System.out.println();
        System.out.println("---------- Test 1: Valid Notification Construction ----------");
        NotificationMessage notification1 = new NotificationMessage.Builder()
                .to("user@example.com")
                .subject("Welcome to the System")
                .body("Hello! Your account has been created successfully.")
                .priority(NotificationMessage.Priority.HIGH)
                .attach("welcome.pdf")
                .attach("setup-guide.pdf")
                .build();
        System.out.println("PASSED: Notification built successfully");
        System.out.println("Result: " + notification1);

        // Test 2: Valid construction with defaults
        System.out.println();
        System.out.println("---------- Test 2: Valid Construction With Defaults ----------");
        NotificationMessage notification2 = new NotificationMessage.Builder()
                .to("admin@system.com")
                .body("System update completed.")
                .build();
        System.out.println("PASSED: Notification built with defaults");
        System.out.println("Result: " + notification2);

        // Test 3: Null recipient validation
        System.out.println();
        System.out.println("---------- Test 3: Validation - Null Recipient ----------");
        try {
            new NotificationMessage.Builder()
                    .subject("Important Message")
                    .body("This should fail")
                    .build();
            System.out.println("FAILED: Should have thrown exception");
        } catch (IllegalStateException e) {
            System.out.println("PASSED: Correctly threw exception: " + e.getMessage());
        }

        // Test 4: Empty recipient validation
        System.out.println();
        System.out.println("---------- Test 4: Validation - Empty Recipient ----------");
        try {
            new NotificationMessage.Builder()
                    .to("   ")
                    .subject("Important Message")
                    .body("This should fail")
                    .build();
            System.out.println("FAILED: Should have thrown exception");
        } catch (IllegalStateException e) {
            System.out.println("PASSED: Correctly threw exception: " + e.getMessage());
        }

        // Test 5: Null body validation
        System.out.println();
        System.out.println("---------- Test 5: Validation - Null Body ----------");
        try {
            new NotificationMessage.Builder()
                    .to("user@example.com")
                    .subject("Important Message")
                    .build();
            System.out.println("FAILED: Should have thrown exception");
        } catch (IllegalStateException e) {
            System.out.println("PASSED: Correctly threw exception: " + e.getMessage());
        }

        // Test 6: Empty body validation
        System.out.println();
        System.out.println("---------- Test 6: Validation - Empty Body ----------");
        try {
            new NotificationMessage.Builder()
                    .to("user@example.com")
                    .subject("Important Message")
                    .body("   ")
                    .build();
            System.out.println("FAILED: Should have thrown exception");
        } catch (IllegalStateException e) {
            System.out.println("PASSED: Correctly threw exception: " + e.getMessage());
        }

        // Test 7: Method chaining fluency
        System.out.println();
        System.out.println("---------- Test 7: Method Chaining Fluency ----------");
        NotificationMessage notification7 = new NotificationMessage.Builder()
                .to("demo@test.com")
                .subject("Fluent Builder Demo")
                .body("This demonstrates method chaining!")
                .priority(NotificationMessage.Priority.LOW)
                .attach("demo1.txt")
                .attach("demo2.txt")
                .build();
        System.out.println("PASSED: Fluent builder works correctly");
        System.out.println("Result: " + notification7);

        System.out.println();
        System.out.println("============================================================");
        System.out.println("ALL TESTS COMPLETED SUCCESSFULLY");
        System.out.println("============================================================");
    }
}