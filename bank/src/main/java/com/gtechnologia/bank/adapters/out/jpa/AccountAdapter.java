package com.gtechnologia.bank.adapters.out.jpa;

import com.gtechnologia.bank.adapters.out.jpa.entity.AccountEntity;
import com.gtechnologia.bank.domain.model.Account;
import com.gtechnologia.bank.application.ports.out.persistence.AccountRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class AccountAdapter implements AccountRepository {

    private final SpringDataAccountRepository repo;
    private final SpringDataClientViewRepository clientRepo;

    public AccountAdapter(SpringDataAccountRepository repo, SpringDataClientViewRepository clientRepo) {
        this.repo = repo;
        this.clientRepo = clientRepo;
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
        return new Account(e.getAccountId(), e.getClientId().getClientId(), e.getBalance());
    }

    private AccountEntity toEntity(Account d) {
        return new AccountEntity(d.getId(), d.getBalance(), clientRepo.getReferenceById(d.getClientId()));
    }
}