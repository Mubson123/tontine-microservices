package com.tontine.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class IdDocumentAlreadyExistsException extends RuntimeException {
    public IdDocumentAlreadyExistsException(String message) {
        super(message);
    }
}
