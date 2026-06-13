package com.iro.labs.banking.exception;

public class TransactionException extends Exception {
    private final String errorCode;
    
    public TransactionException(String message, String errorCode) {
        super("[" + errorCode + "] " + message);
        this.errorCode = errorCode;
    }
    
    public TransactionException(String message, String errorCode, Throwable cause) {
        super("[" + errorCode + "] " + message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}
