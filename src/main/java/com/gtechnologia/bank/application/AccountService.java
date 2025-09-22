package com.gtechnologia.bank.application;

import com.gtechnologia.bank.domain.model.Account;
import com.gtechnologia.bank.domain.ports.in.AccountUseCase;
import com.gtechnologia.bank.domain.ports.out.AccountRepository;
import com.gtechnologia.bank.domain.ports.out.EventPublisher;

import java.math.BigDecimal;
import java.util.UUID;

public final class AccountService implements AccountUseCase {
    private final AccountRepository repo;
    private final EventPublisher publisher;

    public AccountService(AccountRepository repo, EventPublisher publisher) {
        this.repo = repo;
        this.publisher = publisher;
    }

    @Override
    public UUID open(UUID id, BigDecimal initialDeposit) {
        if (repo.findById(id).isPresent()) throw new IllegalStateException("already exists");
        var acc = new Account(id, initialDeposit);
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
