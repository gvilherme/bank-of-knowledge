package com.gtechnologia.bank.domain.exception.account;

import java.util.UUID;

public class AccountDoesNotExistsException extends AccountException {
    public AccountDoesNotExistsException(String message, UUID uuid) {
        super(message, uuid);
    }
}