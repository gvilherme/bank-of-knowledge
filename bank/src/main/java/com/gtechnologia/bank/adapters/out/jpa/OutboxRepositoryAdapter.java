package com.gtechnologia.bank.adapters.out.jpa;

import com.gtechnologia.bank.adapters.out.jpa.entity.IntegrationEventEntity;
import com.gtechnologia.bank.contracts.IntegrationEvent;
import com.gtechnologia.bank.contracts.EventStatus;
import com.gtechnologia.bank.application.ports.out.persistence.OutboxRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OutboxRepositoryAdapter implements OutboxRepository {

    private final SpringDataOutboxWriterRepository outboxRepository;

    public OutboxRepositoryAdapter(SpringDataOutboxWriterRepository outboxRepository) {
        this.outboxRepository = outboxRepository;
    }

    @Override
    public IntegrationEvent append(IntegrationEvent eventRecord) {
        var saved = outboxRepository.save(toIntegrationEventEntity(eventRecord));
        return toIntegrationEvent(saved);
    }

    @Override
    public List<IntegrationEvent> findPendingEvents() {
        Specification<IntegrationEventEntity> specNew = (root, query, cb) -> cb.equal(root.get("status"), EventStatus.NEW);
        Specification<IntegrationEventEntity> specError = (root, query, cb) -> cb.and(cb.equal(root.get("status"), EventStatus.ERROR), cb.lessThan(root.get("retries"), 3));

        Specification<IntegrationEventEntity> combinedSpec = specNew.or(specError);
        return outboxRepository.findAll(combinedSpec).stream().map(this::toIntegrationEvent).toList();
    }

    @Override
    public void markAsSent(IntegrationEvent event) {
        outboxRepository.findById(event.getEventId()).ifPresent(entity -> {
            entity.setEventStatus(EventStatus.SENT);
            outboxRepository.save(entity);
        });
    }

    @Override
    public void incrementRetry(IntegrationEvent event) {
        outboxRepository.findById(event.getEventId()).ifPresent(entity -> {
            entity.setRetries(entity.getRetries() + 1);
            outboxRepository.save(entity);
        });
    }

    private IntegrationEventEntity toIntegrationEventEntity(IntegrationEvent event) {
        return new IntegrationEventEntity(event.getEventId(), event.getType(), event.getAggregateId(), event.getCorrelationId(), event.getCausationId(), event.getAggregateType(), event.getOccurredAt(), event.stringPayload(), event.getVersion(), EventStatus.NEW);
    }

    private IntegrationEvent toIntegrationEvent(IntegrationEventEntity entity) {
        return IntegrationEvent.factory(entity.getEventId(), entity.getType(), entity.getCorrelationId(), entity.getCausationId(), entity.getAggregateId(), entity.getAggregateType(), entity.getOccurredAt(), entity.getPayload(), entity.getVersion());
    }
}
