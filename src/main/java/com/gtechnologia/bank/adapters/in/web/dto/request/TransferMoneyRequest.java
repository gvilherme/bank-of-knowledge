package com.gtechnologia.bank.adapters.in.web.dto.request;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

public record TransferMoneyRequest(@NotNull MoneyRequest moneyRequest, @NotNull UUID accountToTransferId) implements Serializable {
}
