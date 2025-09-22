package com.gtechnologia.bank.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public final class Account {
    private final String id;
    private BigDecimal balance;

    public Account(String id, BigDecimal initial) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id");
        if (initial == null || initial.signum() < 0) throw new IllegalArgumentException("initial >= 0");
        if (initial.compareTo(new BigDecimal("10000.00")) > 0) throw new IllegalArgumentException("initial > 10000.00");
        this.id = id;
        this.balance = initial;
    }

    public String id() { return id; }
    public BigDecimal balance() { return balance; }

    public void deposit(BigDecimal amount) {
        requirePositive(amount);
        balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        requirePositive(amount);
        if (balance.compareTo(amount) < 0) throw new IllegalStateException("insufficient");
        balance = balance.subtract(amount);
    }

    private static void requirePositive(BigDecimal a) {
        if (a == null || a.signum() <= 0) throw new IllegalArgumentException("amount > 0");
    }

    @Override public boolean equals(Object o) {
        return (o instanceof Account other) && Objects.equals(id, other.id);
    }
    @Override public int hashCode() { return Objects.hash(id); }
}