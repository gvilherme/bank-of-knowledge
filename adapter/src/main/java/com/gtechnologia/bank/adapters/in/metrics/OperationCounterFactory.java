package com.gtechnologia.bank.adapters.in.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class OperationCounterFactory {
    private final MeterRegistry registry;

    public OperationCounterFactory(MeterRegistry registry) {
        this.registry = registry;
    }

    public OperationCounterDecorator createCounter(String metricName) {
        switch(metricName){
            case  "opened":
                return new AccountOpenedCounter(registry);
            case  "deposited":
                return new MoneyDepositedCounter(registry);
            case  "withdrawn":
                return new MoneyWithdrawnCounter(registry);
            case  "transferred":
                return new MoneyTransferredCounter(registry);
            default:
                throw new IllegalArgumentException("Unknown metric name: " + metricName);
        }
    }
}
