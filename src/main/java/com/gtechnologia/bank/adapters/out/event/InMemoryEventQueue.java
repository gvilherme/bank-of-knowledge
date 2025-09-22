package com.gtechnologia.bank.adapters.out.event;

import com.gtechnologia.bank.domain.model.Account;
import com.gtechnologia.bank.domain.ports.out.EventPublisher;

import java.util.ArrayList;
import java.util.List;

public final class InMemoryEventQueue implements EventPublisher {
    private final List<Object> store = new ArrayList<>();
    @Override
    public void publish(Object event) {
        store.add(event);
    }

    public Object getNextEvent() {
        return store.getLast();
    }
}
