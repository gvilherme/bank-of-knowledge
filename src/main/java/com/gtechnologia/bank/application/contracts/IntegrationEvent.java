package com.gtechnologia.bank.application.contracts;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public sealed class IntegrationEvent permits ClientRegisteredEvent {
    private final UUID id;
    private final String type;
    private final String aggregateId;
    private final Instant occurredAt;
    @Setter
    private String payload;
    private final int version;

    public IntegrationEvent(
            UUID id, String type, String aggregateId, Instant occurredAt, String payload, int version
    ) {
        this.id = id;
        this.type = type;
        this.aggregateId = aggregateId;
        this.occurredAt = occurredAt;
        this.payload = payload;
        this.version = version;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (IntegrationEvent) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.type, that.type) &&
                Objects.equals(this.aggregateId, that.aggregateId) &&
                Objects.equals(this.occurredAt, that.occurredAt) &&
                Objects.equals(this.payload, that.payload) &&
                this.version == that.version;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, aggregateId, occurredAt, payload, version);
    }

    @Override
    public String toString() {
        return "IntegrationEvent[" +
                "id=" + id + ", " +
                "type=" + type + ", " +
                "aggregateId=" + aggregateId + ", " +
                "occurredAt=" + occurredAt + ", " +
                "payload=" + payload + ", " +
                "version=" + version + ']';
    }
}
