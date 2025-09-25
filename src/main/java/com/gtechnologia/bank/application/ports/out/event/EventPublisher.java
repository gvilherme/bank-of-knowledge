package com.gtechnologia.bank.application.ports.out.event;

import com.gtechnologia.bank.application.contracts.IntegrationEvent;

public interface EventPublisher {
    void publish(IntegrationEvent event);
}

