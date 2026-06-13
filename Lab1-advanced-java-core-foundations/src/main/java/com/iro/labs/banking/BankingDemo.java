package com.iro.labs.banking;

import com.iro.labs.banking.exception.TransactionException;
import com.iro.labs.banking.model.Account;
import com.iro.labs.banking.processor.CsvTransactionProcessor;
import com.iro.labs.banking.service.DatabaseService;
import com.iro.labs.banking.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;

public class BankingDemo {
    private static final Logger log = LoggerFactory.getLogger(BankingDemo.class);
    
    public static void main(String[] args) {
        System.out.println("=== Lab 1.2: Banking Transaction Processor ===\n");
        
        exercise14Demo();
        exercise15Demo();
        exercise16Demo();
    }
    
    private static void exercise14Demo() {
        System.out.println("--- Exercise 1.4: Custom Exception Hierarchy ---");
        
        TransactionService service = new TransactionService();
        Account from = new Account("ACC001", new BigDecimal("500.00"));
        Account to = new Account("ACC002", new BigDecimal("100.00"));
        
        try {
            service.transfer(from, to, new BigDecimal("600.00"));
        } catch (TransactionException e) {
            System.out.println("Caught exception: " + e.getClass().getSimpleName());
            System.out.println("Error code: " + e.getErrorCode());
            System.out.println("Full message: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    private static void exercise15Demo() {
        System.out.println("--- Exercise 1.5: Exception Chaining with MDC ---");
        
        DatabaseService dbService = new DatabaseService();
        
        try {
            dbService.getBalanceFromDatabase("");
        } catch (Exception e) {
            System.out.println("Caught: " + e.getClass().getSimpleName());
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("Original cause: " + e.getCause().getClass().getSimpleName());
                System.out.println("Original message: " + e.getCause().getMessage());
            }
        }
        
        System.out.println("\nCheck logs/banking.log for MDC transactionId entries");
        System.out.println();
    }
    
    private static void exercise16Demo() {
        System.out.println("--- Exercise 1.6: CSV Processing with Error Handling ---");
        
        CsvTransactionProcessor processor = new CsvTransactionProcessor();
        processor.processFile("data/transactions.csv", "logs/failed_transactions.csv");
        
        System.out.println("\nCheck logs/failed_transactions.csv for error details");
        System.out.println();
    }
}
