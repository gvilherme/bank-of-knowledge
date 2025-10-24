package com.gtechnologia.bank.application.ports.out.event;

import com.gtechnologia.bank.contracts.IntegrationEvent;

public interface EventBroker {
    void send(IntegrationEvent<?> event) throws Exception;
}
