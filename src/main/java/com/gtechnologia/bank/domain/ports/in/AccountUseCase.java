package com.gtechnologia.bank.domain.ports.in;

import java.math.BigDecimal;

public interface AccountUseCase {
    public void deposit(String id, BigDecimal deposit);
    String open(String id, BigDecimal initialDeposit);
}
