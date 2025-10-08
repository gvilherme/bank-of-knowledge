package com.gtechnologia.bank.adapters.in.metrics;

import io.micrometer.core.instrument.MeterRegistry;

public class MoneyTransferredCounter extends OperationCounterDecorator {
    public MoneyTransferredCounter(MeterRegistry registry) {
        super(registry, "bank.accounts.transferred");
    }
}
