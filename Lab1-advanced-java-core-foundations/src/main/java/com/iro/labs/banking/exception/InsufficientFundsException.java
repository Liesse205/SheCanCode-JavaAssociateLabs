package com.iro.labs.banking.exception;

public class InsufficientFundsException extends TransactionException {
    public InsufficientFundsException(String message, String errorCode) {
        super(message, errorCode);
    }
    
    public InsufficientFundsException(String message, String errorCode, Throwable cause) {
        super(message, errorCode, cause);
    }
}
