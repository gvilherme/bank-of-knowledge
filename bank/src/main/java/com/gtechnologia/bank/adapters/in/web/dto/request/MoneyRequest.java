package com.gtechnologia.bank.adapters.in.web.dto.request;

import com.gtechnologia.bank.domain.model.account.Money;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Currency;

public record MoneyRequest(
        @NotNull @DecimalMin("0.01") BigDecimal amount,
        String currencyCode
) {
    public Currency getCurrency() {
        return currencyCode == null ? Currency.getInstance("BRL") : Currency.getInstance(currencyCode);
    }

    public Money toMoney() {
        return new Money(amount, getCurrency());
    }
}
