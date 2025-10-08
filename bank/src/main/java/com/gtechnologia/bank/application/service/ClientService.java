package com.gtechnologia.bank.application.service;

import com.gtechnologia.bank.application.ports.in.ClientUseCase;
import com.gtechnologia.bank.application.ports.out.persistence.ClientRepository;
import com.gtechnologia.bank.application.ports.out.event.EventPublisher;
import com.gtechnologia.bank.contracts.ClientRegisteredEvent;
import com.gtechnologia.bank.domain.model.Client;
import com.gtechnologia.bank.domain.model.ClientInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class ClientService implements ClientUseCase {
    private final ClientRepository clientRepository;
    private final EventPublisher eventPublisher;
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    public ClientService(ClientRepository clientRepository, EventPublisher eventPublisher) {
        this.clientRepository = clientRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public UUID register(ClientInformation clientInformation) {
        logger.info("Registering a new client");
        var client = new Client(UUID.randomUUID(), clientInformation);
        clientRepository.save(client);
        eventPublisher.publish(ClientRegisteredEvent.pendingFromClient(client));
        return null;
    }
}
