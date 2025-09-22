package com.gtechnologia.bank.domain.ports.in;

import java.math.BigDecimal;

public interface DepositUseCase {
    public void deposit(String id, BigDecimal deposit);
}
