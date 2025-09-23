package com.gtechnologia.bank.domain.exception.account;

import java.util.UUID;

public class InsufficientBalanceException extends AccountException {
    public InsufficientBalanceException(String message, UUID uuid) {
        super(message, uuid);
    }
}
