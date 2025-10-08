package com.gtechnologia.bank.contracts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public final class AccountOpenedEvent extends IntegrationEvent {
    private final AccountOpenedPayload payload;

    public AccountOpenedEvent(UUID eventId, String correlationId, String causationId,
                              String aggregateId, String aggregateType, Instant occurredAt,
                              UUID accountId, UUID clientId, int version) {
        super(eventId, "AccountOpened", correlationId, causationId, aggregateId, aggregateType, occurredAt, null, version);
        this.payload = new AccountOpenedPayload(accountId, clientId);
        this.setPayload(payload);
    }

    public record AccountOpenedPayload(UUID accountId, UUID clientId) {}

    public static AccountOpenedEvent fromEntity(UUID eventId, String correlationId, String causationId,
                                                String aggregateId, String aggregateType, Instant occurredAt,
                                                String payload, int version) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            AccountOpenedPayload openedPayload = mapper.readValue(payload, AccountOpenedPayload.class);
            return new AccountOpenedEvent(
                    eventId,
                    correlationId,
                    causationId,
                    aggregateId,
                    aggregateType,
                    occurredAt,
                    openedPayload.accountId(),
                    openedPayload.clientId(),
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