package com.tontine.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IdCardExpiredException extends RuntimeException {
    public IdCardExpiredException(String message) {
        super(message);
    }
}
