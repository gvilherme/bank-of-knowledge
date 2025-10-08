package com.gtechnologia.bank.contracts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public final class AccountRejectedEvent extends IntegrationEvent {
    private final AccountRejectedPayload payload;

    public AccountRejectedEvent(UUID eventId, String correlationId, String causationId,
                                String aggregateId, String aggregateType, Instant occurredAt,
                                UUID accountId, UUID clientId, String reason, int version) {
        super(eventId, "AccountRejected", correlationId, causationId, aggregateId, aggregateType, occurredAt, null, version);
        this.payload = new AccountRejectedPayload(accountId, clientId, reason);
        this.setPayload(payload);
    }

    public record AccountRejectedPayload(UUID accountId, UUID clientId, String reason) {}

    public static AccountRejectedEvent fromEntity(UUID eventId, String correlationId, String causationId,
                                                  String aggregateId, String aggregateType, Instant occurredAt,
                                                  String payload, int version) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            AccountRejectedPayload rejectedPayload = mapper.readValue(payload, AccountRejectedPayload.class);
            return new AccountRejectedEvent(
                    eventId,
                    correlationId,
                    causationId,
                    aggregateId,
                    aggregateType,
                    occurredAt,
                    rejectedPayload.accountId(),
                    rejectedPayload.clientId(),
                    rejectedPayload.reason(),
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


