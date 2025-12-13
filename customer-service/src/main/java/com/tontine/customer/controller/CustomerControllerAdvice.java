package com.tontine.customer.controller;

import com.tontine.customer.exception.CustomerAlreadyExistsException;
import com.tontine.customer.exception.CustomerNotFoundException;
import com.tontine.customer.model.ApiError;
import jakarta.validation.ConstraintViolationException;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.MethodNotAllowedException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomerControllerAdvice {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotSupported(@NonNull HttpRequestMethodNotSupportedException ex,
                                                             @NonNull WebRequest request) {
        ApiError apiError = new ApiError()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .path(request.getDescription(false))
                .message(ex.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(apiError);
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity<ApiError> handleMethodNotAllowed(@NonNull MethodNotAllowedException ex,
                                                           @NonNull WebRequest request) {
        ApiError apiError = new ApiError()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .path(request.getDescription(false))
                .message(ex.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(apiError);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleIllegalState(@NonNull IllegalStateException ex, @NonNull WebRequest request) {
        ApiError apiError = new ApiError()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getDescription(false))
                .message(ex.getLocalizedMessage());
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(@NonNull IllegalArgumentException ex,
                                                          @NonNull WebRequest request) {
        ApiError apiError = new ApiError()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getLocalizedMessage())
                .path(request.getDescription(false));
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(@NonNull MethodArgumentNotValidException ex,
                                                           @NonNull WebRequest request) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        ApiError apiError = new ApiError()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getDescription(false))
                .message(ex.getLocalizedMessage())
                .errors(errors);
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(@NonNull ConstraintViolationException ex,
                                                              @NonNull WebRequest request) {
        Map<String, Object> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(error -> errors.put(error.getPropertyPath().toString(), error.getMessage()));
        ApiError apiError = new ApiError()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getDescription(false))
                .message(ex.getLocalizedMessage())
                .errors(errors);
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllUncaught(@NonNull Exception ex,
                                                      @NonNull WebRequest request) {
        ApiError apiError = new ApiError()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getLocalizedMessage())
                .path(request.getDescription(false));
        return ResponseEntity.internalServerError().body(apiError);
    }

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleCustomerAlreadyExists(@NonNull CustomerAlreadyExistsException ex,
                                                                @NonNull WebRequest request) {
        ApiError apiError = new ApiError()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .path(request.getDescription(false))
                .message(ex.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiError> handleCustomerNotFound(@NonNull CustomerNotFoundException ex,
                                                           @NonNull WebRequest request) {
        ApiError apiError = new ApiError()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .path(request.getDescription(false))
                .message(ex.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }
}
