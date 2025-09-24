package com.gtechnologia.bank.adapters.in.web.controller;

import com.gtechnologia.bank.domain.exception.account.AccountDoesNotExistsException;
import com.gtechnologia.bank.domain.exception.account.AccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AccountDoesNotExistsException.class)
    public ResponseEntity<ErrorResponse> handleAccountDoesNotExistsException(AccountDoesNotExistsException ex) {
        logger.error("Account not found: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "Account not found: " + ex.getMessage(),
                ex.getUuid(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<ErrorResponse> handleAccountException(AccountException ex) {
        logger.error("Account error: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "Account error: " + ex.getMessage(),
                ex.getUuid(),
                Instant.now()
        );
        return ResponseEntity.status(ex.getCause() == null ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        logger.error("Internal server error: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse(
                "Internal server error: " + ex.getMessage(),
                null,
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    public record ErrorResponse(String message, UUID accountId, Instant timestamp) {}
}