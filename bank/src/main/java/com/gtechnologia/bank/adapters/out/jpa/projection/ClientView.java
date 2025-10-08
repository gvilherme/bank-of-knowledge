package com.gtechnologia.bank.adapters.out.jpa.projection;

import com.gtechnologia.bank.domain.model.KycStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "client_view", schema = "accounts", indexes = {
    @Index(name = "idx_client_view_last_event_id", columnList = "lastEventId")
})
public class ClientView {
    @Id
    @Column(name = "client_id")
    private UUID clientId;
    @Enumerated(EnumType.STRING)
    private KycStatus kycStatus;
    private Instant createdAt;
    private Instant updatedAt;
    private String lastEventId;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
