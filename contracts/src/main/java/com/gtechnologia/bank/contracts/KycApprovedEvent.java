package com.gtechnologia.bank.contracts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public final class KycApprovedEvent extends IntegrationEvent {
    private final KycApprovedPayload payload;

    public KycApprovedEvent(UUID eventId, String correlationId, String causationId,
                            String aggregateId, String aggregateType, Instant occurredAt,
                            UUID clientId, int version) {
        super(eventId, "KycApproved", correlationId, causationId, aggregateId, aggregateType, occurredAt, null, version);
        this.payload = new KycApprovedPayload(clientId);
        this.setPayload(payload);
    }

    public record KycApprovedPayload(UUID clientId) {}

    public static KycApprovedEvent fromEntity(UUID eventId, String correlationId, String causationId,
                                              String aggregateId, String aggregateType, Instant occurredAt,
                                              String payload, int version) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            KycApprovedPayload approvedPayload = mapper.readValue(payload, KycApprovedPayload.class);
            return new KycApprovedEvent(
                    eventId,
                    correlationId,
                    causationId,
                    aggregateId,
                    aggregateType,
                    occurredAt,
                    approvedPayload.clientId(),
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
