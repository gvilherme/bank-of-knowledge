package com.gtechnologia.bank.domain.exception.account;

import java.util.UUID;

public class InvalidDestinationException extends AccountException {
    public InvalidDestinationException(String message, UUID uuid) {
        super(message, uuid);
    }
}