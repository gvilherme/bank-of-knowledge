package com.gtechnologia.bank.domain.model;

import com.gtechnologia.bank.domain.exception.account.InsufficientBalanceException;
import com.gtechnologia.bank.domain.exception.account.InvalidAmountException;
import com.gtechnologia.bank.domain.exception.account.InvalidDestinationException;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Getter
public final class Account {
    private final UUID id;
    private final UUID clientId;
    private Money balance;

    public Account(UUID id, UUID clientId, Money initial) {
        if (id == null) throw new IllegalArgumentException("id cannot be null");
        if (initial == null || initial.amount().signum() < 0)
            throw new InvalidAmountException("initial balance must be >= 0", id);
        if (initial.compareTo(new Money(new BigDecimal("10000.00"), initial.currency())) > 0)
            throw new InvalidAmountException("initial balance must not be > 10000.00", id);
        if (clientId == null) throw new IllegalArgumentException("clientId cannot be null");
        this.clientId = clientId;
        this.id = id;
        this.balance = initial;
    }

    public void deposit(Money amount) {
        requirePositive(amount);
        balance = new Money(getBalance().amount().add(amount.amount()), getBalance().currency());
    }

    public void withdraw(Money amount) {
        requirePositive(amount);
        if (getBalance().compareTo(amount) < 0)
            throw new InsufficientBalanceException("origin account has insufficient balance", this.getId());
        balance = balance.subtract(amount);
        if (getBalance().amount().unscaledValue().equals(BigDecimal.ZERO.unscaledValue())) {
            balance = new Money(BigDecimal.ZERO, getBalance().currency()); // normalize -0.00 to 0.00
        }
    }

    public void transferTo(Account to, Money amount) {
        if (to == null) throw new InvalidDestinationException("to cannot be null", null);
        if (to.equals(this)) throw new InvalidDestinationException("cannot transfer to the same account", this.getId());
        requirePositive(amount);
        if (this.balance.compareTo(amount) < 0)
            throw new InsufficientBalanceException("origin account has insufficient balance", this.getId());
        this.withdraw(amount);
        to.deposit(amount);
    }

    private static void requirePositive(Money a) {
        if (a == null || a.amount().signum() <= 0) throw new InvalidAmountException("amount > 0", null);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Account other) && Objects.equals(getId(), other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}