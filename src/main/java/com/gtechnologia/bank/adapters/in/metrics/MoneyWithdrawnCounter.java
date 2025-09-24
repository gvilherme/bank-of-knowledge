package com.gtechnologia.bank.adapters.in.metrics;

import io.micrometer.core.instrument.MeterRegistry;

public class MoneyWithdrawnCounter extends OperationCounterDecorator {
    public MoneyWithdrawnCounter(MeterRegistry registry) {
        super(registry, "bank.accounts.withdrawn");
    }
}
