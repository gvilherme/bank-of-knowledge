package com.gtechnologia.bank.domain.ports.out;

public interface EventPublisher {
    void publish(Object event);
}
