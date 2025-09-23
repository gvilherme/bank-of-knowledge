package com.gtechnologia.bank.domain.ports.in;

import java.math.BigDecimal;
import java.util.UUID;

public interface AccountUseCase {
    void deposit(UUID id, BigDecimal deposit);
    UUID openAccount(BigDecimal initialDeposit);
    void withdraw(UUID id, BigDecimal amount);
}
