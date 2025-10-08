package com.gtechnologia.bank.application.ports.out.persistence;

import com.gtechnologia.bank.contracts.IntegrationEvent;

import java.util.List;

public interface OutboxRepository {
    IntegrationEvent append(IntegrationEvent eventRecord);

    List<IntegrationEvent> findPendingEvents();

    void markAsSent(IntegrationEvent event);

    void incrementRetry(IntegrationEvent event);
}
