package com.gtechnologia.bank.domain.model;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

public final class Account {
    private final UUID id;
    private Money balance;

    public Account(UUID id, Money initial) {
        if (id == null) throw new IllegalArgumentException("id cannot be null");
        if (initial == null || initial.amount().signum() < 0) throw new IllegalArgumentException("initial >= 0");
        if (initial.compareTo(new Money( new BigDecimal("10000.00"), initial.currency())) > 0) throw new IllegalArgumentException("initial > 10000.00");
        this.id = id;
        this.balance = initial;
    }

    public UUID id() { return id; }
    public Money balance() { return balance; }

    public void deposit(Money amount) {
        requirePositive(amount);
        balance = new Money(balance.amount().add(amount.amount()), balance.currency());
    }

    public void withdraw(Money amount) {
        requirePositive(amount);
        if (balance.compareTo(amount) < 0) throw new IllegalStateException("insufficient");
        balance = balance.subtract(amount);
        if(balance.amount().unscaledValue().equals(BigDecimal.ZERO.unscaledValue())) {
            balance = new Money(BigDecimal.ZERO, balance.currency()); // normalize -0.00 to 0.00
        }
    }

    private static void requirePositive(Money a) {
        if (a == null || a.amount().signum() <= 0) throw new IllegalArgumentException("amount > 0");
    }

    @Override public boolean equals(Object o) {
        return (o instanceof Account other) && Objects.equals(id, other.id);
    }
    @Override public int hashCode() { return Objects.hash(id); }
}