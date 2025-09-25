package com.gtechnologia.bank.application.contracts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtechnologia.bank.domain.model.Client;

import java.time.Instant;
import java.util.UUID;

public final class ClientRegisteredEvent extends IntegrationEvent {

    public ClientRegisteredEvent(UUID id, String type, String aggregateId, Instant occurredAt, String payload, int version) throws JsonProcessingException {
        super(id, type, aggregateId, occurredAt, payload, version);
    }

    public static ClientRegisteredEvent from (Client client) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return new ClientRegisteredEvent(
                    UUID.randomUUID(),
                    "ClientRegisteredEvent",
                    "",
                    Instant.now(),
                    mapper.writeValueAsString(new ClientRegisteredPayload(client.getId(), client.getClientInformation().firstName())),
                    1
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public record ClientRegisteredPayload(UUID clientId, String name) { }
}
