package com.gtechnologia.bank.application.service;

import com.gtechnologia.bank.application.ports.in.ClientUseCase;
import com.gtechnologia.bank.application.ports.out.log.LoggerPort;
import com.gtechnologia.bank.application.ports.out.persistence.ClientRepository;
import com.gtechnologia.bank.application.ports.out.event.EventPublisher;
import com.gtechnologia.bank.contracts.ClientEvent;
import com.gtechnologia.bank.contracts.IntegrationEventFactory;
import com.gtechnologia.bank.domain.model.client.Client;
import com.gtechnologia.bank.domain.model.client.ClientInformation;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ClientService implements ClientUseCase {
    private final ClientRepository clientRepository;
    private final EventPublisher eventPublisher;
    private final LoggerPort logger;

    public ClientService(ClientRepository clientRepository, EventPublisher eventPublisher, LoggerPort logger) {
        this.clientRepository = clientRepository;
        this.eventPublisher = eventPublisher;
        this.logger = logger;
    }

    @Override
    public UUID register(ClientInformation clientInformation) {
        logger.info("Registering a new client");
        var client = new Client(UUID.randomUUID(), clientInformation);
        clientRepository.save(client);
        eventPublisher.publish(IntegrationEventFactory.create(new ClientEvent(client.getClientId(), client.getClientInformation(), client.getKycStatus()), UUID.randomUUID().toString(), "Client", "ClientRegistered", 2, UUID.randomUUID().toString(), null, client.getClientId(), Instant.now()));
        return client.getClientId();
    }

    @Override
    public Client getClientById(UUID clientId) {
        logger.info("Retrieving a client by ID");
        return clientRepository.findById(clientId).orElseThrow(() -> new IllegalArgumentException("Client not found with ID: " + clientId));
    }

    @Override
    public List<Client> getClients(int page, int size) {
        logger.info("Retrieving clients with pagination");
        return clientRepository.findAll(page, size).orElseThrow(() -> new IllegalArgumentException("No clients found"));
    }
}
