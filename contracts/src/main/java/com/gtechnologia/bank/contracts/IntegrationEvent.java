package com.gtechnologia.bank.contracts;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public sealed abstract class IntegrationEvent permits ClientRegisteredEvent, KycApprovedEvent, KycRejectedEvent, AccountOpenedEvent, AccountRejectedEvent {
    private final UUID eventId;
    private final String type;
    private final String correlationId;
    private final String causationId;
    private final String aggregateId;
    private final String aggregateType;
    private final Instant occurredAt;
    @Setter
    protected Object payload;
    private final int version;

    public IntegrationEvent(
            UUID eventId, String type, String correlationId, String causationId, String aggregateId, String aggregateType, Instant occurredAt, String payload, int version
    ) {
        this.eventId = eventId;
        this.type = type;
        this.correlationId = correlationId;
        this.causationId = causationId;
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
        this.occurredAt = occurredAt;
        this.payload = payload;
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IntegrationEvent that = (IntegrationEvent) o;
        return version == that.version && Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, version);
    }

    public abstract String stringPayload();

    public static IntegrationEvent factory(UUID eventId, String type, String correlationId, String causationId, String aggregateId, String aggregateType, Instant occurredAt, String payload, int version) {
        return switch (type) {
            case "ClientRegistered" -> ClientRegisteredEvent.fromEntity(
                    eventId,
                    correlationId,
                    causationId,
                    aggregateId,
                    aggregateType,
                    occurredAt,
                    payload,
                    version
            );
            case "KycApproved" -> KycApprovedEvent.fromEntity(
                    eventId,
                    correlationId,
                    causationId,
                    aggregateId,
                    aggregateType,
                    occurredAt,
                    payload,
                    version
            );
            case "KycRejected" -> KycRejectedEvent.fromEntity(
                    eventId,
                    correlationId,
                    causationId,
                    aggregateId,
                    aggregateType,
                    occurredAt,
                    payload,
                    version
            );
            case "AccountOpened" -> AccountOpenedEvent.fromEntity(
                    eventId,
                    correlationId,
                    causationId,
                    aggregateId,
                    aggregateType,
                    occurredAt,
                    payload,
                    version
            );
            case "AccountRejected" -> AccountRejectedEvent.fromEntity(
                    eventId,
                    correlationId,
                    causationId,
                    aggregateId,
                    aggregateType,
                    occurredAt,
                    payload,
                    version
            );
            default -> throw new IllegalArgumentException("Unknown event type: " + type);
        };
    }

}
