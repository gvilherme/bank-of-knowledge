package com.gtechnologia.bank.adapters.in.metrics;

import io.micrometer.core.instrument.MeterRegistry;

public class MoneyDepositedCounter extends OperationCounterDecorator {
    public MoneyDepositedCounter(MeterRegistry registry) {
        super(registry, "bank.accounts.deposited");
    }
}
