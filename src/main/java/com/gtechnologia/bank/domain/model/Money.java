package com.gtechnologia.bank.domain.model;

import java.math.BigDecimal;
import java.util.Currency;

public record Money(BigDecimal amount, Currency currency) {
    public int compareTo(Money amount) {
        return this.amount.compareTo(amount.amount());
    }

    public Money subtract(Money amount) {
        return new Money(this.amount.subtract(amount.amount()), this.currency);
    }
}
