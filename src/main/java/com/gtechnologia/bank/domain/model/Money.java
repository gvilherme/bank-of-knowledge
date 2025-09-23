package com.gtechnologia.bank.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.util.Currency;

@Embeddable
public record Money(
        @Column(nullable = false, precision = 19, scale = 2) BigDecimal amount, Currency currency) {
    public int compareTo(Money amount) {
        return this.amount.compareTo(amount.amount());
    }

    public Money subtract(Money amount) {
        return new Money(this.amount.subtract(amount.amount()), this.currency);
    }
}
