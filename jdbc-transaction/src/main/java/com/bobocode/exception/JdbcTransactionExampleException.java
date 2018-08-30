package com.bobocode.exception;

public class JdbcTransactionExampleException extends RuntimeException {
    public JdbcTransactionExampleException(String message) {
        super(message);
    }

    public JdbcTransactionExampleException(String message, Throwable cause) {
        super(message, cause);
    }
}
