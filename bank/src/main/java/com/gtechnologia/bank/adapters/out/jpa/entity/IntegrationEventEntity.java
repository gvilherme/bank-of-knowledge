package com.gtechnologia.bank.adapters.out.jpa.entity;

import com.gtechnologia.bank.contracts.EventStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "outbox_events", schema = "outbox")
public class IntegrationEventEntity {
    @Id
    private UUID eventId;
    private String type;
    @Column(name = "aggregate_id")
    private String aggregateId;
    @Column(name = "correlation_id")
    private String correlationId;
    @Column(name = "causation_id")
    private String causationId;
    @Column(name = "aggregate_type")
    private String aggregateType;
    @Column(name = "occurred_at")
    private Instant occurredAt;
    private String payload;
    private int version;
    @Column(name = "status", columnDefinition = "smallint")
    @Setter
    private EventStatus eventStatus;
    @Column(name = "updated_at")
    private Instant updatedAt;
    @Column(name = "retries", columnDefinition = "smallint")
    @Setter
    private Integer retries;

    public IntegrationEventEntity(UUID eventId, String type, String aggregateId, String correlationId, String causationId, String aggregateType, Instant occurredAt, String payload, int version, EventStatus eventStatus) {
        this.eventId = eventId;
        this.type = type;
        this.correlationId = correlationId;
        this.causationId = causationId;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.occurredAt = occurredAt;
        this.payload = payload;
        this.version = version;
        this.eventStatus = eventStatus;
    }

    @PrePersist
    protected void onInsert(){
        updatedAt = Instant.now();
        retries = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
