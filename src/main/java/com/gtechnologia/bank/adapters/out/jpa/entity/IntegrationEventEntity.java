package com.gtechnologia.bank.adapters.out.jpa.entity;

import com.gtechnologia.bank.application.contracts.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "outbox_events")
public class IntegrationEventEntity {
    @Id
    private UUID id;
    private String type;
    @Column(name = "aggregate_id")
    private String aggregateId;
    @Column(name = "occurred_at")
    private Instant occurredAt;
    private String payload;
    private int version;
    @Column(name = "status", columnDefinition = "smallint")
    @Setter
    private Status status;
    @Column(name = "updated_at")
    private Instant updatedAt;
    @Column(name = "retries", columnDefinition = "smallint")
    @Setter
    private Integer retries;

    public IntegrationEventEntity(UUID id, String type, String aggregateId, Instant occurredAt, String payload, int version, Status status) {
        this.id = id;
        this.type = type;
        this.aggregateId = aggregateId;
        this.occurredAt = occurredAt;
        this.payload = payload;
        this.version = version;
        this.status = status;
    }

    public IntegrationEventEntity() {

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
