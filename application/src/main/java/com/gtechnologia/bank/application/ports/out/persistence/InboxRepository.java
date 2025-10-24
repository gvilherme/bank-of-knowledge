package com.gtechnologia.bank.application.ports.out.persistence;

import com.gtechnologia.bank.contracts.IntegrationEvent;

import java.util.List;

public interface InboxRepository {
    void markProcessed(String eventId, String consumer);
    boolean tryStart(String eventId, String consumer);
}
