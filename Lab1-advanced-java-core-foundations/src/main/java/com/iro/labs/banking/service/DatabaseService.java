package com.iro.labs.banking.service;

import com.iro.labs.banking.exception.DataAccessException;
import com.iro.labs.banking.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DatabaseService {
    private static final Logger log = LoggerFactory.getLogger(DatabaseService.class);
    private final Map<String, Account> accountDatabase = new HashMap<>();
    
    public void saveAccount(Account account) {
        accountDatabase.put(account.getId(), account);
    }
    
    public Account findAccountById(String id) {
        return accountDatabase.get(id);
    }
    
    public BigDecimal getBalanceFromDatabase(String accountId) throws DataAccessException {
        String transactionId = UUID.randomUUID().toString();
        MDC.put("transactionId", transactionId);
        
        try {
            log.debug("Fetching balance for account: {}", accountId);
            simulateDatabaseAccess(accountId);
            Account account = accountDatabase.get(accountId);
            if (account == null) {
                throw new SQLException("Account not found: " + accountId);
            }
            return account.getBalance();
        } catch (SQLException e) {
            log.error("Database error while fetching balance for account: {}", accountId, e);
            throw new DataAccessException("Failed to fetch balance for account: " + accountId, e);
        } finally {
            MDC.clear();
        }
    }
    
    private void simulateDatabaseAccess(String accountId) throws SQLException {
        if (accountId == null || accountId.isEmpty()) {
            throw new SQLException("Invalid account identifier");
        }
    }
}
