package com.gtechnologia.bank.contracts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtechnologia.bank.domain.model.Client;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public final class ClientRegisteredEvent extends IntegrationEvent {

    private final KycStatus kycStatus;
    private final ClientRegisteredPayload payload;

    public ClientRegisteredEvent(UUID eventId, String type, String correlationId, String causationId,
                                 String aggregateId, String aggregateType, Instant occurredAt,
                                 Client client, KycStatus kycStatus, int version) {
        super(eventId, type, correlationId, causationId, aggregateId, aggregateType, occurredAt, null, version);
        this.kycStatus = kycStatus;
        this.payload = ClientRegisteredPayload.fromClient(client, kycStatus);
        this.setPayload(payload);
    }

    public record ClientRegisteredPayload(UUID clientId, String name, KycStatus kycStatus) {
        public static ClientRegisteredPayload fromClient(Client client, KycStatus kycStatus) {
            return new ClientRegisteredPayload(client.getId(), client.getClientInformation().firstName(), kycStatus);
        }
    }

    public static ClientRegisteredEvent pendingFromClient(Client client){
        return new ClientRegisteredEvent(
                UUID.randomUUID(),
                "ClientRegisteredEvent",
                null,
                null,
                client.getId().toString(),
                "Client",
                Instant.now(),
                client,
                KycStatus.PENDING,
                1
        );
    }

    public static ClientRegisteredEvent fromEntity(UUID eventId, String type, String correlationId, String causationId, String aggregateId, String aggregateType, Instant occurredAt, String payload, int version){
        ObjectMapper mapper = new ObjectMapper();
        try {
            ClientRegisteredPayload clientPayload = mapper.readValue(payload, ClientRegisteredPayload.class);
            return new ClientRegisteredEvent(
                    eventId,
                    type,
                    correlationId,
                    causationId,
                    aggregateId,
                    aggregateType,
                    occurredAt,
                    new Client(clientPayload.clientId(), null),
                    clientPayload.kycStatus(),
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
