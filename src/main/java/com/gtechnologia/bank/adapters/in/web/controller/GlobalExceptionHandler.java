package com.gtechnologia.bank.adapters.in.web.controller;

import com.gtechnologia.bank.domain.exception.account.AccountDoesNotExistsException;
import com.gtechnologia.bank.domain.exception.account.AccountException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountDoesNotExistsException.class)
    public ResponseEntity<ErrorResponse> handleAccountDoesNotExistsException(AccountDoesNotExistsException ex) {
        ErrorResponse error = new ErrorResponse(
                "Account not found: " + ex.getMessage(),
                ex.getUuid(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<ErrorResponse> handleAccountException(AccountException ex) {
        ErrorResponse error = new ErrorResponse(
                "Account error: " + ex.getMessage(),
                ex.getUuid(),
                Instant.now()
        );
        return ResponseEntity.status(ex.getCause() == null ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    public record ErrorResponse(String message, UUID accountId, Instant timestamp) {}
}