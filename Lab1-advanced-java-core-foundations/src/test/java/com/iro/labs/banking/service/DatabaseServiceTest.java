package com.iro.labs.banking.service;

import com.iro.labs.banking.exception.DataAccessException;
import com.iro.labs.banking.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseServiceTest {
    private DatabaseService databaseService;
    private Account testAccount;
    
    @BeforeEach
    void setUp() {
        databaseService = new DatabaseService();
        testAccount = new Account("ACC001", new BigDecimal("1000.00"));
        databaseService.saveAccount(testAccount);
    }
    
    @Test
    void testGetBalanceFromDatabase_Success() {
        BigDecimal balance = databaseService.getBalanceFromDatabase(testAccount.getId());
        assertEquals(new BigDecimal("1000.00"), balance);
    }
    
    @Test
    void testGetCauseReturnsOriginalSQLException() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            databaseService.getBalanceFromDatabase("");
        });
        
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof SQLException);
        
        SQLException cause = (SQLException) exception.getCause();
        assertEquals("Invalid account identifier", cause.getMessage());
    }
}
