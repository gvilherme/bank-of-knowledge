package com.gtechnologia.bank.application.ports.in;

import com.gtechnologia.bank.domain.model.Account;
import com.gtechnologia.bank.domain.model.Money;

import java.util.UUID;

public interface AccountUseCase{
    void deposit(UUID id, Money deposit);
    UUID openAccount(Money initialDeposit, UUID clientId);
    void withdraw(UUID id, Money amount);
    Account getAccount(UUID id);
    void transfer(UUID fromAccountId, UUID toAccountId, Money amount);
}
