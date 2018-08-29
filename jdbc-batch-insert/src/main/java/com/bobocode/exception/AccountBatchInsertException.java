package com.bobocode.exception;

public class AccountBatchInsertException extends RuntimeException {
    public AccountBatchInsertException(String message) {
        super(message);
    }

    public AccountBatchInsertException(String message, Throwable cause) {
        super(message, cause);
    }
}
