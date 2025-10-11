package com.gtechnologia.bank.adapters.out.event;

import com.gtechnologia.bank.contracts.IntegrationEvent;
import com.gtechnologia.bank.application.ports.out.event.EventBroker;

public class InMemoryEventBroker implements EventBroker {

    @Override
    public void send(IntegrationEvent event) throws Exception {

    }
}
