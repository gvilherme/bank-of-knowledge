package com.gtechnologia.bank.adapters.in.web.dto.response;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

public record AccountGetResponse(@NotNull UUID accountId, @NotNull BigDecimal balance, @NotNull Currency currency) implements Serializable {
}
