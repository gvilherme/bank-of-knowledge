package com.gtechnologia.bank.adapters.infrastructure;

import com.gtechnologia.bank.application.ports.in.ClientUseCase;
import com.gtechnologia.bank.application.ports.out.event.EventPublisher;
import com.gtechnologia.bank.application.ports.out.log.LoggerPort;
import com.gtechnologia.bank.application.ports.out.persistence.ClientRepository;
import com.gtechnologia.bank.application.service.ClientService;
import com.gtechnologia.bank.domain.model.client.Client;
import com.gtechnologia.bank.domain.model.client.ClientInformation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public class TxClientUseCase implements ClientUseCase {
    private final ClientService clientService;

    public TxClientUseCase(ClientRepository clientRepository, EventPublisher eventPublisher, LoggerPort logger) {
        this.clientService = new ClientService(clientRepository, eventPublisher, logger);
    }

    @Override
    @Transactional
    public UUID register(ClientInformation clientInformation) {
        return clientService.register(clientInformation);
    }

    @Override
    public Client getClientById(UUID clientId) {
        return clientService.getClientById(clientId);
    }

    @Override
    public List<Client> getClients(int page, int size) {
        return clientService.getClients(page, size);
    }
}
