package com.gtechnologia.bank.adapters.out.service;

import com.gtechnologia.bank.adapters.out.jpa.SpringDataClientRepository;
import com.gtechnologia.bank.adapters.out.jpa.entity.ClientEntity;
import com.gtechnologia.bank.adapters.out.jpa.entity.ClientInformationEntity;
import com.gtechnologia.bank.application.ports.out.persistence.ClientRepository;
import com.gtechnologia.bank.domain.model.client.Client;
import com.gtechnologia.bank.domain.model.client.ClientInformation;
import com.gtechnologia.bank.domain.model.client.DocumentNumber;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ClientAdapter implements ClientRepository {
    private final SpringDataClientRepository clientRepository;
    public ClientAdapter(SpringDataClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client save(Client client) {
        var clientEntity = clientRepository.save(fromClient(client));
        return toClient(clientEntity);
    }

    @Override
    public Optional<Client> findById(UUID id) {
        return clientRepository.findById(id).map(this::toClient);
    }

    @Override
    public Optional<Client> findByDocument(DocumentNumber document) {
        return clientRepository.findByClientInformation_Document(document).map(this::toClient);
    }

    private ClientEntity fromClient(Client client) {
        return new ClientEntity(
                client.getClientId(),
                fromClientInformation(client.getClientInformation()),
                client.getKycStatus()
        );
    }

    private Client toClient(ClientEntity clientEntity) {
        return new Client(
                clientEntity.getId(),
                toClientInformation(clientEntity.getClientInformation()),
                clientEntity.getKycStatus()
        );
    }

    private ClientInformationEntity fromClientInformation(ClientInformation clientInformation) {
        var entity = new ClientInformationEntity();
        entity.setFirstName(clientInformation.firstName());
        entity.setDocument(clientInformation.document());
        entity.setLastName(clientInformation.lastName());
        return entity;
    }

    private ClientInformation toClientInformation(ClientInformationEntity clientInformationEntity) {
        return new ClientInformation(
                clientInformationEntity.getFirstName(),
                clientInformationEntity.getLastName(),
                clientInformationEntity.getDocument()
        );
    }
}
