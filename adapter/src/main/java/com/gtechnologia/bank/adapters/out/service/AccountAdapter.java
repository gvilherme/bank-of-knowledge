package com.gtechnologia.bank.adapters.out.service;

import com.gtechnologia.bank.adapters.out.jpa.SpringDataAccountRepository;
import com.gtechnologia.bank.adapters.out.jpa.SpringDataClientViewRepository;
import com.gtechnologia.bank.adapters.out.jpa.entity.AccountEntity;
import com.gtechnologia.bank.adapters.out.jpa.projection.ClientView;
import com.gtechnologia.bank.domain.model.account.Account;
import com.gtechnologia.bank.application.ports.out.persistence.AccountRepository;
import com.gtechnologia.bank.domain.model.account.AccountStatus;
import com.gtechnologia.bank.domain.model.client.KycStatus;
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
        ClientView clientFromView = clientRepo.getReferenceById(account.getClientId());
        if(account.getStatus().equals(AccountStatus.PENDING)) {
            if(clientFromView.getKycStatus().equals(KycStatus.APPROVED)) {
                account.setStatus(AccountStatus.ACTIVE);
            } else if (clientFromView.getKycStatus().equals(KycStatus.REJECTED)){
                account.setStatus(AccountStatus.REJECTED);
            }
        }
        var saved = repo.save(toEntity(account));
        return toDomain(saved);
    }

    private Account toDomain(AccountEntity e) {
        return new Account(e.getAccountId(), e.getClientId().getClientId(), e.getBalance(), e.getStatus());
    }

    private AccountEntity toEntity(Account d) {
        return new AccountEntity(d.getId(), d.getBalance(), clientRepo.getReferenceById(d.getClientId()), d.getStatus());
    }
}