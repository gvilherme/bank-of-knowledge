package com.gtechnologia.bank.adapters.out.event;

import com.gtechnologia.bank.application.ports.out.event.EventBroker;
import com.gtechnologia.bank.application.ports.out.event.EventPublisher;
import com.gtechnologia.bank.application.contracts.IntegrationEvent;
import com.gtechnologia.bank.application.ports.out.persistence.OutboxRepository;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public final class OutboxEventPublisher implements EventPublisher {
    private final OutboxRepository outboxRepository;
    private final EventBroker brokerAdapter;

    public OutboxEventPublisher(OutboxRepository outboxRepository, EventBroker brokerAdapter) {
        this.outboxRepository = outboxRepository;
        this.brokerAdapter = brokerAdapter;
    }

    @Scheduled(fixedDelay = 5000)
    public void processOutbox() {
        List<IntegrationEvent> events = outboxRepository.findPendingEvents();
        for (IntegrationEvent event : events) {
            try {
                brokerAdapter.send(event);
                outboxRepository.markAsSent(event);
            } catch (Exception e) {
                outboxRepository.incrementRetry(event);
            }
        }
    }

    @Override
    public void publish(IntegrationEvent event) {
        outboxRepository.append(event);
    }
}
