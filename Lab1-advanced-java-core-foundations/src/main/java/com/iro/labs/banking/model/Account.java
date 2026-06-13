package com.iro.labs.banking.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Account {
    private final String id;
    private final String accountNumber;
    private BigDecimal balance;
    private boolean fraudSuspected;
    
    public Account(String accountNumber, BigDecimal initialBalance) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be null or empty");
        }
        if (initialBalance == null) {
            throw new IllegalArgumentException("Initial balance cannot be null");
        }
        if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        
        this.id = UUID.randomUUID().toString();
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.fraudSuspected = false;
    }
    
    public String getId() {
        return id;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    public boolean isFraudSuspected() {
        return fraudSuspected;
    }
    
    public void markFraudSuspected() {
        this.fraudSuspected = true;
    }
    
    public void debit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Debit amount must be positive");
        }
        this.balance = this.balance.subtract(amount);
    }
    
    public void credit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Credit amount must be positive");
        }
        this.balance = this.balance.add(amount);
    }
}
