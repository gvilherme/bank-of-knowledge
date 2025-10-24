package com.gtechnologia.bank.adapters.out.jpa.projection;

import com.gtechnologia.bank.domain.model.client.KycStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "client_kyc_view", schema = "accounts", indexes = {
    @Index(name = "idx_client_kyc_view_last_event_id", columnList = "lastEventId")
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
