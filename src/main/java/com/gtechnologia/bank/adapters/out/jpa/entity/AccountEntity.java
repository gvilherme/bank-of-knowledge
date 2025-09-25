package com.gtechnologia.bank.adapters.out.jpa.entity;

import com.gtechnologia.bank.adapters.out.jpa.converter.MoneyConverter;
import com.gtechnologia.bank.domain.model.Money;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "accounts")
public class AccountEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Setter
    @Convert(converter = MoneyConverter.class)
    @Column(nullable = false, precision = 19, scale = 2)
    private Money balance;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    protected AccountEntity() {}
    public AccountEntity(UUID id, Money balance) { this.id = id; this.balance = balance; }

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
