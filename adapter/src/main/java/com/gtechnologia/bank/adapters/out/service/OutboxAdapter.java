package com.gtechnologia.bank.adapters.out.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtechnologia.bank.adapters.out.jpa.SpringDataOutboxWriterRepository;
import com.gtechnologia.bank.adapters.out.jpa.entity.IntegrationEventEntity;
import com.gtechnologia.bank.contracts.IntegrationEvent;
import com.gtechnologia.bank.contracts.EventStatus;
import com.gtechnologia.bank.application.ports.out.persistence.OutboxRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OutboxAdapter implements OutboxRepository {

    private final SpringDataOutboxWriterRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public OutboxAdapter(SpringDataOutboxWriterRepository outboxRepository, ObjectMapper objectMapper) {
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public IntegrationEvent<?> append(IntegrationEvent<?> eventRecord) {
        var saved = outboxRepository.save(toIntegrationEventEntity(eventRecord));
        return toIntegrationEvent(saved);
    }

    @Override
    public List<IntegrationEvent<?>> findPendingEvents() {
        Specification<IntegrationEventEntity> specNew = (root, query, cb) -> cb.equal(root.get("eventStatus"), EventStatus.NEW);
        Specification<IntegrationEventEntity> specError = (root, query, cb) -> cb.and(cb.equal(root.get("eventStatus"), EventStatus.ERROR), cb.lessThan(root.get("retries"), 3));

        Specification<IntegrationEventEntity> combinedSpec = specNew.or(specError);
        return outboxRepository.findAll(combinedSpec).stream().map(this::toIntegrationEvent).collect(Collectors.toList());
    }

    @Override
    public void markAsSent(IntegrationEvent<?> event) {
        outboxRepository.findById(event.eventId()).ifPresent(entity -> {
            entity.setEventStatus(EventStatus.SENT);
            outboxRepository.save(entity);
        });
    }

    @Override
    public void incrementRetry(IntegrationEvent<?> event) {
        outboxRepository.findById(event.eventId()).ifPresent(entity -> {
            entity.setRetries(entity.getRetries() + 1);
            entity.setEventStatus(EventStatus.ERROR);
            outboxRepository.save(entity);
        });
    }

    private IntegrationEventEntity toIntegrationEventEntity(IntegrationEvent<?> event) {
        String jsonPayload;
        try {
            // prefer a provided JSON string
            jsonPayload = event.stringPayload();
            if (jsonPayload == null || jsonPayload.isBlank()) {
                jsonPayload = objectMapper.writeValueAsString(event.payload());
            } else {
                // validate
                JsonNode node = objectMapper.readTree(jsonPayload);
                if (node == null) {
                    jsonPayload = objectMapper.writeValueAsString(event.payload());
                }
            }
        } catch (Exception e) {
            try {
                jsonPayload = objectMapper.writeValueAsString(event.payload());
            } catch (Exception ex) {
                jsonPayload = "{}";
            }
        }

        return new IntegrationEventEntity(
                event.eventId(),
                event.type(),
                event.aggregateId(),
                event.correlationId(),
                event.causationId(),
                event.aggregateType(),
                event.occurredAt(),
                jsonPayload,
                event.version(),
                EventStatus.NEW
        );
    }

    private IntegrationEvent<?> toIntegrationEvent(IntegrationEventEntity entity) {
        return new IntegrationEvent<>(
                entity.getEventId(),
                entity.getType(),
                entity.getVersion(),
                entity.getAggregateType(),
                entity.getAggregateId(),
                entity.getCorrelationId(),
                entity.getCausationId(),
                entity.getOccurredAt(),
                entity.getPayload()
        );
    }
}
