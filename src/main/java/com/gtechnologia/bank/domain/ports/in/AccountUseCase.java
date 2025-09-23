package com.gtechnologia.bank.domain.ports.in;

import com.gtechnologia.bank.domain.model.Account;
import com.gtechnologia.bank.domain.model.Money;

import java.util.UUID;

public interface AccountUseCase {
    void deposit(UUID id, Money deposit);
    UUID openAccount(Money initialDeposit);
    void withdraw(UUID id, Money amount);
    Account getAccount(UUID id);
}
