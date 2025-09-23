package com.gtechnologia.bank.adapters.out.jpa;

import com.gtechnologia.bank.domain.model.Money;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class AccountEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Embedded
    private Money balance;

    protected AccountEntity() {}
    public AccountEntity(UUID id, Money balance) { this.id = id; this.balance = balance; }

    public UUID getId() { return id; }
    public Money getBalance() { return balance; }
    public void setBalance(Money b) { this.balance = b; }
}
