package com.gtechnologia.bank.adapters.in.web.dto;

import com.gtechnologia.bank.domain.model.Money;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Currency;

public record OpenAccountRequest(@NotNull @DecimalMin("0.00") BigDecimal initialDeposit, String currencyCode) {
    public Currency getCurrency() {
        return currencyCode == null ? Currency.getInstance("BRL") : Currency.getInstance(currencyCode);
    }

    public Money getBalance() {
        return new Money(initialDeposit, getCurrency());
    }
}