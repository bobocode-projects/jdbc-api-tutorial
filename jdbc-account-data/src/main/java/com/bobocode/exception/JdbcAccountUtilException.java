package com.bobocode.exception;

public class JdbcAccountUtilException extends RuntimeException {
    public JdbcAccountUtilException(String message) {
        super(message);
    }

    public JdbcAccountUtilException(String message, Throwable cause) {
        super(message, cause);
    }
}
