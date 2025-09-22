package com.gtechnologia.bank.adapters.out.memory;

import com.gtechnologia.bank.domain.model.Account;
import com.gtechnologia.bank.domain.ports.out.AccountRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class InMemoryAccountRepository implements AccountRepository {
    private final Map<UUID, Account> store = new HashMap<>();

    @Override
    public Optional<Account> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void save(Account account) {
        store.put(account.id(), account);
    }
}