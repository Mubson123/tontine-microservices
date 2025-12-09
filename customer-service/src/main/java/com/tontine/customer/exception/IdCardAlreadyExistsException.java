package com.tontine.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class IdCardAlreadyExistsException extends RuntimeException {
    public IdCardAlreadyExistsException(String message) {
        super(message);
    }
}
