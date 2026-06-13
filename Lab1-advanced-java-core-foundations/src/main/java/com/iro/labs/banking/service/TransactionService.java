package com.iro.labs.banking.service;

import com.iro.labs.banking.exception.InsufficientFundsException;
import com.iro.labs.banking.exception.FraudDetectedException;
import com.iro.labs.banking.exception.TransactionException;
import com.iro.labs.banking.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;

public class TransactionService {
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);
    
    public void transfer(Account from, Account to, BigDecimal amount) 
            throws TransactionException {
        
        if (amount == null) {
            throw new IllegalArgumentException("Transfer amount cannot be null");
        }
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        
        log.debug("Processing transfer from {} to {} for amount {}", 
                  from.getAccountNumber(), to.getAccountNumber(), amount);
        
        if (from.isFraudSuspected()) {
            throw new FraudDetectedException(
                "Fraud suspected on account: " + from.getAccountNumber(),
                "FRD_001"
            );
        }
        
        if (to.isFraudSuspected()) {
            throw new FraudDetectedException(
                "Fraud suspected on destination account: " + to.getAccountNumber(),
                "FRD_002"
            );
        }
        
        if (from.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(
                "Insufficient funds in account: " + from.getAccountNumber() + 
                ". Balance: " + from.getBalance() + ", Required: " + amount,
                "INSF_001"
            );
        }
        
        from.debit(amount);
        to.credit(amount);
        
        log.info("Transfer completed. From: {}, To: {}, Amount: {}", 
                 from.getAccountNumber(), to.getAccountNumber(), amount);
    }
}
