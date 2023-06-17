package com.cards.cards.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(HttpStatus unauthorized, String message) {
        super(message);
    }
}
