package com.cards.cards.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomExceptionHandler  extends RuntimeException {
    public CustomExceptionHandler(String message) {
        super(message);
    }
}