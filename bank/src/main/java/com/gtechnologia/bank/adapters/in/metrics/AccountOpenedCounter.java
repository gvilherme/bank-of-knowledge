package com.gtechnologia.bank.adapters.in.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class AccountOpenedCounter extends OperationCounterDecorator {
    public AccountOpenedCounter(MeterRegistry registry) {
        super(registry, "bank.accounts.opened");
    }
}
