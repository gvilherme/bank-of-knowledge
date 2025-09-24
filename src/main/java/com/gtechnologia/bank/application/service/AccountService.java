package com.gtechnologia.bank.application.service;

import com.gtechnologia.bank.domain.exception.account.AccountDoesNotExistsException;
import com.gtechnologia.bank.domain.model.Account;
import com.gtechnologia.bank.domain.model.Money;
import com.gtechnologia.bank.domain.ports.in.AccountUseCase;
import com.gtechnologia.bank.domain.ports.out.AccountRepositoryPort;
import com.gtechnologia.bank.domain.ports.out.EventPublisher;
import com.gtechnologia.bank.util.WrapAccountException;
import jakarta.transaction.Transactional;

import java.util.UUID;

public class AccountService implements AccountUseCase {
    private final AccountRepositoryPort repo;
    private final EventPublisher publisher;

    public AccountService(AccountRepositoryPort repo, EventPublisher publisher) {
        this.repo = repo;
        this.publisher = publisher;
    }

    @WrapAccountException
    @Override
    public UUID openAccount(Money initialDeposit) {
        var acc = new Account(UUID.randomUUID(), initialDeposit);
        repo.save(acc);
        publisher.publish("Account open with Id: " + acc.getId());
        return acc.getId();
    }

    @WrapAccountException
    @Override
    public void withdraw(UUID id, Money amount) {
        var acc = repo.findById(id).orElseThrow(() -> new AccountDoesNotExistsException("no such account", id));
        acc.withdraw(amount);
        repo.save(acc);
    }

    @WrapAccountException
    @Override
    public void deposit(UUID id, Money amount) {
        var acc = repo.findById(id).orElseThrow(() -> new AccountDoesNotExistsException("no such account", id));
        acc.deposit(amount);
        repo.save(acc);
    }

    @WrapAccountException
    @Override
    public Account getAccount(UUID id) {
        return repo.findById(id).orElseThrow(() -> new AccountDoesNotExistsException("no such account", id));
    }

    @Transactional
    @WrapAccountException
    @Override
    public void transfer(UUID fromAccountId, UUID toAccountId, Money amount) {
        var acc = repo.findById(fromAccountId).orElseThrow(() -> new AccountDoesNotExistsException("no such account", fromAccountId));
        var to = repo.findById(toAccountId).orElseThrow(() -> new AccountDoesNotExistsException("no such account", toAccountId));
        acc.transferTo(to, amount);
        repo.save(acc);
        repo.save(to);
    }

}
