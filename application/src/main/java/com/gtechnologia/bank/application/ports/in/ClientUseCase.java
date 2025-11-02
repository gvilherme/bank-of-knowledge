package com.gtechnologia.bank.application.ports.in;

import com.gtechnologia.bank.domain.model.client.Client;
import com.gtechnologia.bank.domain.model.client.ClientInformation;

import java.util.List;
import java.util.UUID;

public interface ClientUseCase {
    UUID register(ClientInformation clientInformation);
    Client getClientById(UUID clientId);
    List<Client> getClients(int page, int size);
}
