package com.gtechnologia.bank.application.ports.out.persistence;

import com.gtechnologia.bank.domain.model.account.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    Optional<Account> findById(UUID id);
    Account save(Account account);
}