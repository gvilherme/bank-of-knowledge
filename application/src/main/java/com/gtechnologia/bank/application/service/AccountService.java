package com.gtechnologia.bank.application.service;

import com.gtechnologia.bank.application.ports.out.log.LoggerPort;
import com.gtechnologia.bank.domain.exception.account.AccountDoesNotExistsException;
import com.gtechnologia.bank.domain.model.account.Account;
import com.gtechnologia.bank.domain.model.account.AccountStatus;
import com.gtechnologia.bank.domain.model.account.Money;
import com.gtechnologia.bank.application.ports.in.AccountUseCase;
import com.gtechnologia.bank.application.ports.out.persistence.AccountRepository;
import com.gtechnologia.bank.application.ports.out.event.EventPublisher;
import com.gtechnologia.bank.util.WrapAccountException;

import java.util.UUID;

public class AccountService implements AccountUseCase {
    private final LoggerPort logger ;
    private final AccountRepository repo;
    private final EventPublisher publisher;

    public AccountService(AccountRepository repo, EventPublisher publisher, LoggerPort logger) {
        this.repo = repo;
        this.publisher = publisher;
        this.logger = logger;
    }

    @WrapAccountException
    @Override
    public UUID openAccount(Money initialDeposit, UUID clientId) {
        logger.info("Opening account");
        var acc = new Account(UUID.randomUUID(), clientId, initialDeposit, AccountStatus.PENDING);
        repo.save(acc);
        logger.info("Account open with Id: {}", acc.getId());
        return acc.getId();
    }

    @WrapAccountException
    @Override
    public void withdraw(UUID id, Money amount) {
        logger.info("Withdrawing account from account with Id: {}", id);
        var acc = repo.findById(id).orElseThrow(() -> new AccountDoesNotExistsException("no such account", id));
        acc.withdraw(amount);
        repo.save(acc);
        logger.info("Withdrawn account from account with Id: {}", id);
    }

    @WrapAccountException
    @Override
    public void deposit(UUID id, Money amount) {
        logger.info("Depositing account to account with Id: {}", id);
        var acc = repo.findById(id).orElseThrow(() -> new AccountDoesNotExistsException("no such account", id));
        acc.deposit(amount);
        repo.save(acc);
        logger.info("Deposited account to account with Id: {}", id);
    }

    @WrapAccountException
    @Override
    public Account getAccount(UUID id) {
        logger.info("Getting account info with Id: " + id);
        var acc = repo.findById(id).orElseThrow(() -> new AccountDoesNotExistsException("no such account", id));
        logger.info("Got account with Id: {}", id);
        return acc;
    }

    @WrapAccountException
    @Override
    public void transfer(UUID fromAccountId, UUID toAccountId, Money amount) {
        logger.info("Transferring balance from account with Id: {}", fromAccountId);
        var acc = repo.findById(fromAccountId).orElseThrow(() -> new AccountDoesNotExistsException("no such account", fromAccountId));
        var to = repo.findById(toAccountId).orElseThrow(() -> new AccountDoesNotExistsException("no such account", toAccountId));
        acc.transferTo(to, amount);
        repo.save(acc);
        repo.save(to);
        logger.info("Transferred balance from account with Id: {}", fromAccountId);
    }

}
