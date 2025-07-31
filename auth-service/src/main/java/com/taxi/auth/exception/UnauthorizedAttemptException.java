package com.taxi.auth.exception;

public class UnauthorizedAttemptException extends RuntimeException {
    public UnauthorizedAttemptException(String message) {
        super(message);
    }
}
