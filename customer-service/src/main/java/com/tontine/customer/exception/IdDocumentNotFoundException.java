package com.tontine.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class IdDocumentNotFoundException extends RuntimeException {
    public IdDocumentNotFoundException(String message) {
        super(message);
    }
}
