package com.gtechnologia.bank.domain.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public final class Account {
    private final UUID id;
    private BigDecimal balance;

    public Account(UUID id, BigDecimal initial) {
        if (id == null) throw new IllegalArgumentException("id cannot be null");
        if (initial == null || initial.signum() < 0) throw new IllegalArgumentException("initial >= 0");
        if (initial.compareTo(new BigDecimal("10000.00")) > 0) throw new IllegalArgumentException("initial > 10000.00");
        this.id = id;
        this.balance = initial;
    }

    public UUID id() { return id; }
    public BigDecimal balance() { return balance; }

    public void deposit(BigDecimal amount) {
        requirePositive(amount);
        balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        requirePositive(amount);
        if (balance.compareTo(amount) < 0) throw new IllegalStateException("insufficient");
        balance = balance.subtract(amount);
        if(balance.unscaledValue().equals(BigDecimal.ZERO.unscaledValue())) {
            balance = BigDecimal.ZERO; // normalize -0.00 to 0.00
        }
    }

    private static void requirePositive(BigDecimal a) {
        if (a == null || a.signum() <= 0) throw new IllegalArgumentException("amount > 0");
    }

    @Override public boolean equals(Object o) {
        return (o instanceof Account other) && Objects.equals(id, other.id);
    }
    @Override public int hashCode() { return Objects.hash(id); }
}