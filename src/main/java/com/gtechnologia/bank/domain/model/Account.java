package com.gtechnologia.bank.domain.model;

import com.gtechnologia.bank.domain.exception.account.InsufficientBalanceException;
import com.gtechnologia.bank.domain.exception.account.InvalidAmountException;
import com.gtechnologia.bank.domain.exception.account.InvalidDestinationException;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

public final class Account {
    private final UUID id;
    private Money balance;

    public Account(UUID id, Money initial) {
        if (id == null) throw new IllegalArgumentException("id cannot be null");
        if (initial == null || initial.amount().signum() < 0) throw new InvalidAmountException("initial balance must be >= 0", id);
        if (initial.compareTo(new Money(new BigDecimal("10000.00"), initial.currency())) > 0)
            throw new InvalidAmountException("initial balance must not be > 10000.00", id);
        this.id = id;
        this.balance = initial;
    }

    public UUID id() {
        return id;
    }

    public Money balance() {
        return balance;
    }

    public void deposit(Money amount) {
        requirePositive(amount);
        balance = new Money(balance.amount().add(amount.amount()), balance.currency());
    }

    public void withdraw(Money amount) {
        requirePositive(amount);
        if (balance.compareTo(amount) < 0) throw new InsufficientBalanceException("origin account has insufficient balance", this.id);
        balance = balance.subtract(amount);
        if (balance.amount().unscaledValue().equals(BigDecimal.ZERO.unscaledValue())) {
            balance = new Money(BigDecimal.ZERO, balance.currency()); // normalize -0.00 to 0.00
        }
    }

    public void transferTo(Account to, Money amount) {
        if (to == null) throw new InvalidDestinationException("to cannot be null", null);
        requirePositive(amount);
        if (this.balance.compareTo(amount) < 0)
            throw new InsufficientBalanceException("origin account has insufficient balance", this.id);
        this.withdraw(amount);
        to.deposit(amount);
    }

    private static void requirePositive(Money a) {
        if (a == null || a.amount().signum() <= 0) throw new InvalidAmountException("amount > 0", null);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Account other) && Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}