package com.gtechnologia.bank.application.ports.out.persistence;

import com.gtechnologia.bank.domain.model.Client;
import com.gtechnologia.bank.domain.model.DocumentNumber;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository {
    Client save(Client client);
    Optional<Client> findById(UUID id);
    Optional<Client> findByDocument(DocumentNumber document);
}
