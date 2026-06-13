package com.iro.labs.banking.service;

import com.iro.labs.banking.exception.*;
import com.iro.labs.banking.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionServiceTest {
    private TransactionService transactionService;
    private Account fromAccount;
    private Account toAccount;
    
    @BeforeEach
    void setUp() {
        transactionService = new TransactionService();
        fromAccount = new Account("ACC001", new BigDecimal("1000.00"));
        toAccount = new Account("ACC002", new BigDecimal("500.00"));
    }
    
    @Test
    void testSuccessfulTransfer() throws TransactionException {
        BigDecimal amount = new BigDecimal("200.00");
        
        transactionService.transfer(fromAccount, toAccount, amount);
        
        assertEquals(new BigDecimal("800.00"), fromAccount.getBalance());
        assertEquals(new BigDecimal("700.00"), toAccount.getBalance());
    }
    
    @Test
    void testInsufficientFundsThrowsException() {
        BigDecimal amount = new BigDecimal("2000.00");
        
        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, () -> {
            transactionService.transfer(fromAccount, toAccount, amount);
        });
        
        assertEquals("INSF_001", exception.getErrorCode());
        assertTrue(exception.getMessage().contains("INSF_001"));
    }
    
    @Test
    void testFraudSuspectedFromAccountThrowsException() {
        fromAccount.markFraudSuspected();
        BigDecimal amount = new BigDecimal("100.00");
        
        FraudDetectedException exception = assertThrows(FraudDetectedException.class, () -> {
            transactionService.transfer(fromAccount, toAccount, amount);
        });
        
        assertEquals("FRD_001", exception.getErrorCode());
    }
    
    @Test
    void testFraudSuspectedToAccountThrowsException() {
        toAccount.markFraudSuspected();
        BigDecimal amount = new BigDecimal("100.00");
        
        FraudDetectedException exception = assertThrows(FraudDetectedException.class, () -> {
            transactionService.transfer(fromAccount, toAccount, amount);
        });
        
        assertEquals("FRD_002", exception.getErrorCode());
    }
    
    @Test
    void testNegativeAmountThrowsException() {
        BigDecimal amount = new BigDecimal("-100.00");
        
        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.transfer(fromAccount, toAccount, amount);
        });
    }
}
