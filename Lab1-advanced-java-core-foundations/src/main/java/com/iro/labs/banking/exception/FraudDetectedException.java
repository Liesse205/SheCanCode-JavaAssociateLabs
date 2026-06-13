package com.iro.labs.banking.exception;

public class FraudDetectedException extends TransactionException {
    public FraudDetectedException(String message, String errorCode) {
        super(message, errorCode);
    }
    
    public FraudDetectedException(String message, String errorCode, Throwable cause) {
        super(message, errorCode, cause);
    }
}
