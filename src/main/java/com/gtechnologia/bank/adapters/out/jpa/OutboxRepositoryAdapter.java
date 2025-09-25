package com.gtechnologia.bank.adapters.out.jpa;

import com.gtechnologia.bank.adapters.out.jpa.entity.IntegrationEventEntity;
import com.gtechnologia.bank.application.contracts.IntegrationEvent;
import com.gtechnologia.bank.application.contracts.Status;
import com.gtechnologia.bank.application.ports.out.persistence.OutboxRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

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
        Specification<IntegrationEventEntity> specNew = (root, query, cb) ->
                cb.equal(root.get("status"), Status.NEW);
        Specification<IntegrationEventEntity> specError = (root, query, cb) ->
                cb.and(
                        cb.equal(root.get("status"), Status.ERROR),
                        cb.lessThan(root.get("retries"), 3)
                );

        Specification<IntegrationEventEntity> combinedSpec = where(specNew).or(specError);
        return outboxRepository.findAll(combinedSpec)
                .stream()
                .map(this::toIntegrationEvent)
                .toList();
    }

    @Override
    public void markAsSent(IntegrationEvent event) {
        outboxRepository.findById(event.getId()).ifPresent(entity -> {
            entity.setStatus(Status.SENT);
            outboxRepository.save(entity);
        });
    }

    @Override
    public void incrementRetry(IntegrationEvent event) {
        outboxRepository.findById(event.getId()).ifPresent(entity -> {
            entity.setRetries(entity.getRetries() + 1);
            outboxRepository.save(entity);
        });
    }

    private IntegrationEventEntity toIntegrationEventEntity(IntegrationEvent event) {
        return new IntegrationEventEntity(event.getId(), event.getType(), event.getAggregateId(), event.getOccurredAt(), event.getPayload(), event.getVersion(), Status.NEW);
    }

    private IntegrationEvent toIntegrationEvent(IntegrationEventEntity entity) {
        return new IntegrationEvent(entity.getId(), entity.getType(), entity.getAggregateId(), entity.getOccurredAt(), entity.getPayload(), entity.getVersion());
    }
}
