package com.gtechnologia.bank.adapters.out.jpa;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class AccountEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    protected AccountEntity() {}
    public AccountEntity(UUID id, BigDecimal balance) { this.id = id; this.balance = balance; }

    public UUID getId() { return id; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal b) { this.balance = b; }
}
