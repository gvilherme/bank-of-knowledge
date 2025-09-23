package com.gtechnologia.bank.adapters.in.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record MoneyRequest(@NotNull @DecimalMin("0.01") BigDecimal amount) { }