package com.gtechnologia.bank.application.ports.out.event;

import com.gtechnologia.bank.contracts.IntegrationEvent;

public interface EventPublisher {
    void publish(IntegrationEvent event);
}

