package com.gtechnologia.bank.domain.exception.account;

import lombok.Getter;

import java.io.Serial;
import java.util.UUID;

@Getter
public abstract class AccountException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID uuid;

    public AccountException(String message, UUID uuid) {
        super(message);
        this.uuid = uuid;
    }

    public AccountException(String message, UUID uuid, Throwable cause) {
        super(message, cause);
        this.uuid = uuid;
    }
}
