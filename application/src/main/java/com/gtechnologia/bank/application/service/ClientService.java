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
        return null;
    }
}
