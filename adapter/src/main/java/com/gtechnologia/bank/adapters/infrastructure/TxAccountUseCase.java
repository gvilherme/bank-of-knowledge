package com.gtechnologia.bank.adapters.infrastructure;

import com.gtechnologia.bank.application.ports.in.AccountUseCase;
import com.gtechnologia.bank.application.ports.out.event.EventPublisher;
import com.gtechnologia.bank.application.ports.out.log.LoggerPort;
import com.gtechnologia.bank.application.ports.out.persistence.AccountRepository;
import com.gtechnologia.bank.application.service.AccountService;
import com.gtechnologia.bank.domain.model.account.Account;
import com.gtechnologia.bank.domain.model.account.Money;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.logging.Logger;

public class TxAccountUseCase implements AccountUseCase {
    private final AccountService accountService;

    public TxAccountUseCase(AccountRepository repo, EventPublisher eventPublisher, LoggerPort logger) {
        this.accountService = new AccountService(repo, eventPublisher, logger);
    }

    @Override
    @Transactional
    public void deposit(UUID id, Money deposit) {
        accountService.deposit(id, deposit);
    }

    @Override
    @Transactional
    public UUID openAccount(Money initialDeposit, UUID clientId) {
        return accountService.openAccount(initialDeposit, clientId);
    }

    @Override
    @Transactional
    public void withdraw(UUID id, Money amount) {
        accountService.withdraw(id, amount);
    }

    @Override
    @Transactional(readOnly = true)
    public Account getAccount(UUID id) {
        return accountService.getAccount(id);
    }

    @Override
    @Transactional
    public void transfer(UUID fromAccountId, UUID toAccountId, Money amount) {
        accountService.transfer(fromAccountId, toAccountId, amount);
    }
}
