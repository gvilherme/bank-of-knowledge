package com.gtechnologia.bank.contracts;

import java.time.Instant;
import java.util.UUID;

public record IntegrationEvent <T>(
        UUID eventId,
        String type,            // ex: "ClientRegistered"
        int version,            // ex: 2
        String aggregateType,   // ex: "Client"
        String aggregateId,     // UUID em string
        String correlationId,
        String causationId,
        Instant occurredAt,
        T payload
) {
    public String stringPayload() {
        return payload.toString();
    }
}
