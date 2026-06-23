package com.shecan.lab22.scenario2_notification_payment.exercise25_strategy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentProcessorTest {

    private static final String SEPARATOR = "============================================================";

    private void printHeader(String title) {
        System.out.println();
        System.out.println(SEPARATOR);
        System.out.println("TEST: " + title);
        System.out.println(SEPARATOR);
    }

    private void printResult(String status, String details) {
        System.out.println("RESULT: " + status);
        System.out.println("DETAILS: " + details);
    }

    @Test
    void testCreditCardStrategy() {
        printHeader("Credit Card Strategy - Payment Processing");
        
        PaymentRequest request = new PaymentRequest("CC-12345", 100.0, "USD");
        PaymentStrategy strategy = new CreditCardStrategy();
        PaymentProcessor processor = new PaymentProcessor(strategy);
        
        System.out.println("Request: " + request);
        PaymentResult result = processor.processPayment(request);
        
        printResult(result.isSuccess() ? "SUCCESS" : "FAILED", result.getMessage());
        System.out.println("Fee: $" + result.getFee());
        System.out.println("Total Amount: $" + result.getTotalAmount());
        System.out.println(SEPARATOR);
        
        assertTrue(result.isSuccess());
        assertEquals(2.5, result.getFee(), 0.001);
        assertEquals(102.5, result.getTotalAmount(), 0.001);
    }

    @Test
    void testBankTransferStrategy() {
        printHeader("Bank Transfer Strategy - Payment Processing");
        
        PaymentRequest request = new PaymentRequest("BT-98765", 500.0, "USD");
        PaymentStrategy strategy = new BankTransferStrategy();
        PaymentProcessor processor = new PaymentProcessor(strategy);
        
        System.out.println("Request: " + request);
        PaymentResult result = processor.processPayment(request);
        
        printResult(result.isSuccess() ? "SUCCESS" : "FAILED", result.getMessage());
        System.out.println("Fee: $" + result.getFee());
        System.out.println("Total Amount: $" + result.getTotalAmount());
        System.out.println(SEPARATOR);
        
        assertTrue(result.isSuccess());
        assertEquals(5.0, result.getFee(), 0.001);
        assertEquals(505.0, result.getTotalAmount(), 0.001);
    }

    @Test
    void testMobileMoneyStrategy() {
        printHeader("Mobile Money Strategy - Payment Processing");
        
        PaymentRequest request = new PaymentRequest("MM-45678", 75.0, "USD");
        PaymentStrategy strategy = new MobileMoneyStrategy();
        PaymentProcessor processor = new PaymentProcessor(strategy);
        
        System.out.println("Request: " + request);
        PaymentResult result = processor.processPayment(request);
        
        printResult(result.isSuccess() ? "SUCCESS" : "FAILED", result.getMessage());
        System.out.println("Fee: $" + result.getFee());
        System.out.println("Total Amount: $" + result.getTotalAmount());
        System.out.println(SEPARATOR);
        
        assertTrue(result.isSuccess());
        assertEquals(1.125, result.getFee(), 0.001);
        assertEquals(76.125, result.getTotalAmount(), 0.001);
    }

    @Test
    void testSwapStrategiesAtRuntime() {
        printHeader("Runtime Strategy Swap - Payment Processing");
        
        // Use different account IDs for each strategy to pass validation
        PaymentRequest creditCardRequest = new PaymentRequest("CC-12345", 200.0, "USD");
        PaymentRequest bankTransferRequest = new PaymentRequest("BT-12345", 200.0, "USD");
        PaymentRequest mobileMoneyRequest = new PaymentRequest("MM-12345", 200.0, "USD");
        
        PaymentProcessor processor = new PaymentProcessor(new CreditCardStrategy());
        
        System.out.println("Initial Strategy: Credit Card");
        System.out.println("Request: " + creditCardRequest);
        
        // Process with Credit Card
        PaymentResult result1 = processor.processPayment(creditCardRequest);
        System.out.println("Credit Card Fee: $" + result1.getFee());
        System.out.println("Credit Card Total: $" + result1.getTotalAmount());
        System.out.println("Credit Card Result: " + result1);
        
        // Swap to Bank Transfer
        System.out.println();
        System.out.println("Swapping strategy to Bank Transfer...");
        processor.setStrategy(new BankTransferStrategy());
        
        // Process with Bank Transfer
        PaymentResult result2 = processor.processPayment(bankTransferRequest);
        System.out.println("Bank Transfer Fee: $" + result2.getFee());
        System.out.println("Bank Transfer Total: $" + result2.getTotalAmount());
        System.out.println("Bank Transfer Result: " + result2);
        
        // Swap to Mobile Money
        System.out.println();
        System.out.println("Swapping strategy to Mobile Money...");
        processor.setStrategy(new MobileMoneyStrategy());
        
        // Process with Mobile Money
        PaymentResult result3 = processor.processPayment(mobileMoneyRequest);
        System.out.println("Mobile Money Fee: $" + result3.getFee());
        System.out.println("Mobile Money Total: $" + result3.getTotalAmount());
        System.out.println("Mobile Money Result: " + result3);
        
        System.out.println(SEPARATOR);
        printResult("SUCCESS", "All three strategies applied correctly with different fees");
        System.out.println("Credit Card Fee: " + result1.getFee());
        System.out.println("Bank Transfer Fee: " + result2.getFee());
        System.out.println("Mobile Money Fee: " + result3.getFee());
        System.out.println(SEPARATOR);
        
        // Verify each strategy applied the correct fee
        // Credit Card: 200 * 0.025 = 5.0
        assertEquals(5.0, result1.getFee(), 0.001, "Credit Card fee should be 5.0");
        
        // Bank Transfer: Fixed fee of 5.0
        assertEquals(5.0, result2.getFee(), 0.001, "Bank Transfer fee should be 5.0");
        
        // Mobile Money: 200 * 0.015 = 3.0
        assertEquals(3.0, result3.getFee(), 0.001, "Mobile Money fee should be 3.0");
    }

    @Test
    void testCreditCardValidationFailure() {
        printHeader("Credit Card Strategy - Validation Failure");
        
        PaymentRequest request = new PaymentRequest("", 100.0, "USD");
        PaymentStrategy strategy = new CreditCardStrategy();
        PaymentProcessor processor = new PaymentProcessor(strategy);
        
        System.out.println("Request (invalid): " + request);
        PaymentResult result = processor.processPayment(request);
        
        printResult("FAILED", result.getMessage());
        assertFalse(result.isSuccess());
        System.out.println(SEPARATOR);
    }

    @Test
    void testBankTransferValidationFailure() {
        printHeader("Bank Transfer Strategy - Validation Failure");
        
        PaymentRequest request = new PaymentRequest("BT-12345", 5.0, "USD");
        PaymentStrategy strategy = new BankTransferStrategy();
        PaymentProcessor processor = new PaymentProcessor(strategy);
        
        System.out.println("Request (invalid): " + request);
        PaymentResult result = processor.processPayment(request);
        
        printResult("FAILED", result.getMessage());
        assertFalse(result.isSuccess());
        System.out.println(SEPARATOR);
    }

    @Test
    void testMobileMoneyValidationFailure() {
        printHeader("Mobile Money Strategy - Validation Failure");
        
        PaymentRequest request = new PaymentRequest("INVALID-123", 75.0, "USD");
        PaymentStrategy strategy = new MobileMoneyStrategy();
        PaymentProcessor processor = new PaymentProcessor(strategy);
        
        System.out.println("Request (invalid): " + request);
        PaymentResult result = processor.processPayment(request);
        
        printResult("FAILED", result.getMessage());
        assertFalse(result.isSuccess());
        System.out.println(SEPARATOR);
    }

    @Test
    void testMobileMoneyMinimumFee() {
        printHeader("Mobile Money Strategy - Minimum Fee Applied");
        
        PaymentRequest request = new PaymentRequest("MM-12345", 10.0, "USD");
        PaymentStrategy strategy = new MobileMoneyStrategy();
        PaymentProcessor processor = new PaymentProcessor(strategy);
        
        System.out.println("Request: " + request);
        PaymentResult result = processor.processPayment(request);
        
        printResult(result.isSuccess() ? "SUCCESS" : "FAILED", result.getMessage());
        System.out.println("Fee: $" + result.getFee());
        System.out.println("Total Amount: $" + result.getTotalAmount());
        System.out.println(SEPARATOR);
        
        assertTrue(result.isSuccess());
        assertEquals(0.50, result.getFee(), 0.001);
        assertEquals(10.50, result.getTotalAmount(), 0.001);
    }
}