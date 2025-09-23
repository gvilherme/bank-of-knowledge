package com.gtechnologia.bank.adapters.out.jpa;

import com.gtechnologia.bank.domain.model.Account;
import com.gtechnologia.bank.domain.ports.out.AccountRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class AccountJpaAdapter implements AccountRepositoryPort {

    private final SpringDataAccountRepository repo;

    public AccountJpaAdapter(SpringDataAccountRepository repo) {
        this.repo = repo;
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return repo.findById(id).map(this::toDomain);
    }

    @Override
    public Account save(Account account) {
        var saved = repo.save(toEntity(account));
        return toDomain(saved);
    }

    private Account toDomain(AccountEntity e) {
        return new Account(e.getId(), e.getBalance());
    }

    private AccountEntity toEntity(Account d) {
        return new AccountEntity(d.id(), d.balance());
    }
}