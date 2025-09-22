package com.gtechnologia.bank.application;

import com.gtechnologia.bank.domain.model.Account;
import com.gtechnologia.bank.domain.ports.in.DepositUseCase;
import com.gtechnologia.bank.domain.ports.in.OpenAccountUseCase;
import com.gtechnologia.bank.domain.ports.out.AccountRepository;
import com.gtechnologia.bank.domain.ports.out.EventPublisher;

import java.math.BigDecimal;

public final class AccountService implements OpenAccountUseCase, DepositUseCase {
    private final AccountRepository repo;
    private final EventPublisher publisher;

    public AccountService(AccountRepository repo, EventPublisher publisher) {
        this.repo = repo;
        this.publisher = publisher;
    }

    @Override
    public String open(String id, BigDecimal initialDeposit) {
        if (repo.findById(id).isPresent()) throw new IllegalStateException("already exists");
        var acc = new Account(id, initialDeposit);
        repo.save(acc);
        publisher.publish("Account open with Id: " + acc.id());
        return acc.id();
    }

    @Override
    public void deposit(String id, BigDecimal amount) {
        var acc = repo.findById(id).orElseThrow(() -> new IllegalStateException("no such account"));
        acc.deposit(amount);
        repo.save(acc);
    }

    // Exponha outros casos de uso depois: deposit, withdraw, transfer...
}
