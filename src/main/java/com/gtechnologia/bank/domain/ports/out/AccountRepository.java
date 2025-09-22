package com.gtechnologia.bank.domain.ports.out;

import com.gtechnologia.bank.domain.model.Account;

import java.util.Optional;

public interface AccountRepository {
    Optional<Account> findById(String id);
    void save(Account account);
}