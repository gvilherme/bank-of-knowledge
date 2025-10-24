package com.gtechnologia.bank.adapters.out.jpa.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Entity
@Table(name="inbox", schema="inbox")
@NoArgsConstructor
public class InboxEntryEntity {
    @EmbeddedId
    private InboxKey key;
    private Instant receivedAt;
    private Instant processedAt;
    private String lastError;

    public InboxEntryEntity(InboxKey key) {
        this.key = key;
        this.receivedAt = Instant.now();
    }

    public void markProcessed(){ this.processedAt = Instant.now(); this.lastError = null; }
}
