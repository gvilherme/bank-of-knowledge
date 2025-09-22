package com.gtechnologia.bank.domain.ports.in;

import java.math.BigDecimal;

public interface OpenAccountUseCase {
    String open(String id, BigDecimal initialDeposit);
}
