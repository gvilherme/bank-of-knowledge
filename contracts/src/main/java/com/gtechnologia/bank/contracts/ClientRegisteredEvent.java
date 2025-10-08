package com.gtechnologia.bank.contracts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtechnologia.bank.domain.model.Client;
import com.gtechnologia.bank.domain.model.ClientInformation;
import com.gtechnologia.bank.domain.model.DocumentNumber;
import com.gtechnologia.bank.domain.model.KycStatus;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public final class ClientRegisteredEvent extends IntegrationEvent {

    private final ClientRegisteredPayload payload;

    public ClientRegisteredEvent(UUID eventId, String correlationId, String causationId,
                                 String aggregateId, String aggregateType, Instant occurredAt,
                                 Client client, int version) {
        super(eventId, "ClientRegistered", correlationId, causationId, aggregateId, aggregateType, occurredAt, null, version);
        this.payload = ClientRegisteredPayload.fromClient(client);
        this.setPayload(payload);
    }

    public record ClientRegisteredPayload(UUID clientId, String name, String lastName, String document, KycStatus kycStatus) {
        public static ClientRegisteredPayload fromClient(Client client) {
            return new ClientRegisteredPayload(client.getClientId(), client.getClientInformation().firstName(), client.getClientInformation().lastName(), client.getClientInformation().document().number(), client.getKycStatus());
        }
    }

    public static ClientRegisteredEvent pendingFromClient(Client client){
        return new ClientRegisteredEvent(
                UUID.randomUUID(),
                null,
                null,
                client.getClientId().toString(),
                "Client",
                Instant.now(),
                client,
                1
        );
    }

    public static ClientRegisteredEvent fromEntity(UUID eventId, String correlationId, String causationId, String aggregateId, String aggregateType, Instant occurredAt, String payload, int version){
        ObjectMapper mapper = new ObjectMapper();
        try {
            ClientRegisteredPayload clientPayload = mapper.readValue(payload, ClientRegisteredPayload.class);
            return new ClientRegisteredEvent(
                    eventId,
                    correlationId,
                    causationId,
                    aggregateId,
                    aggregateType,
                    occurredAt,
                    new Client(clientPayload.clientId(), new ClientInformation(clientPayload.name(), clientPayload.lastName(), new DocumentNumber(clientPayload.document()))),
                    version
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize payload", e);
        }
    }

    @Override
    public String stringPayload() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
    }
}
