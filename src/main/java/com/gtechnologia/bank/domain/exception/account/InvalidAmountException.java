package com.gtechnologia.bank.domain.exception.account;

import java.util.UUID;

public class InvalidAmountException extends AccountException {
    public InvalidAmountException(String message, UUID uuid) {
        super(message, uuid);
    }
}
