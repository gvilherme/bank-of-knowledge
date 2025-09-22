package com.gtechnologia.bank.domain.ports.out;

import com.gtechnologia.bank.domain.model.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    Optional<Account> findById(UUID id);
    void save(Account account);
}