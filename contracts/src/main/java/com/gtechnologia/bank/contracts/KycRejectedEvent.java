package com.gtechnologia.bank.contracts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public final class KycRejectedEvent extends IntegrationEvent {
    private final KycRejectedPayload payload;

    public KycRejectedEvent(UUID eventId, String correlationId, String causationId,
                            String aggregateId, String aggregateType, Instant occurredAt,
                            UUID clientId, String reason, int version) {
        super(eventId, "KycRejected", correlationId, causationId, aggregateId, aggregateType, occurredAt, null, version);
        this.payload = new KycRejectedPayload(clientId, reason);
        this.setPayload(payload);
    }

    public record KycRejectedPayload(UUID clientId, String reason) {}

    public static KycRejectedEvent fromEntity(UUID eventId, String correlationId, String causationId,
                                              String aggregateId, String aggregateType, Instant occurredAt,
                                              String payload, int version) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            KycRejectedPayload rejectedPayload = mapper.readValue(payload, KycRejectedPayload.class);
            return new KycRejectedEvent(
                    eventId,
                    correlationId,
                    causationId,
                    aggregateId,
                    aggregateType,
                    occurredAt,
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
