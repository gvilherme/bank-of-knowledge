package com.gtechnologia.bank.adapters.in.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

public abstract class OperationCounterDecorator {
    protected final Counter specificCounter;
    protected final Counter totalOperations;

    public OperationCounterDecorator(MeterRegistry registry, String specificMetricName) {
        this.specificCounter = Counter.builder(specificMetricName).register(registry);
        this.totalOperations = Counter.builder("bank.operations.total").register(registry);
    }

    public void increment() {
        specificCounter.increment();
        totalOperations.increment();
    }
}
