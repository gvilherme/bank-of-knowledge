package com.gtechnologia.bank.application.service;

import com.gtechnologia.bank.domain.model.Account;
import com.gtechnologia.bank.domain.ports.in.AccountUseCase;
import com.gtechnologia.bank.domain.ports.out.AccountRepositoryPort;
import com.gtechnologia.bank.domain.ports.out.EventPublisher;

import java.math.BigDecimal;
import java.util.UUID;

public final class AccountService implements AccountUseCase {
    private final AccountRepositoryPort repo;
    private final EventPublisher publisher;

    public AccountService(AccountRepositoryPort repo, EventPublisher publisher) {
        this.repo = repo;
        this.publisher = publisher;
    }

    @Override
    public UUID openAccount(BigDecimal initialDeposit) {
        var acc = new Account(UUID.randomUUID(), initialDeposit);
        repo.save(acc);
        publisher.publish("Account open with Id: " + acc.id());
        return acc.id();
    }

    @Override
    public void withdraw(UUID id, BigDecimal amount) {
        var acc = repo.findById(id).orElseThrow(() -> new IllegalStateException("no such account"));
        acc.withdraw(amount);
        repo.save(acc);
    }

    @Override
    public void deposit(UUID id, BigDecimal amount) {
        var acc = repo.findById(id).orElseThrow(() -> new IllegalStateException("no such account"));
        acc.deposit(amount);
        repo.save(acc);
    }

}
