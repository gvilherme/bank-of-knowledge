package com.gtechnologia.bank.adapters.in.web.dto.request;

import com.gtechnologia.bank.domain.model.account.Money;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

public record OpenAccountRequest(@NotNull @DecimalMin("0.00") BigDecimal initialDeposit, String currencyCode, @NotNull UUID clientId) {
    public Currency getCurrency() {
        return currencyCode == null ? Currency.getInstance("BRL") : Currency.getInstance(currencyCode);
    }

    public Money getBalance() {
        return new Money(initialDeposit, getCurrency());
    }
}