package com.gtechnologia.bank.contracts;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class IntegrationEventFactory {

    private IntegrationEventFactory() {}

    public static <T> IntegrationEvent<T> create(T payload) {
        return create(payload, null, null, null, 1, null, null, UUID.randomUUID(), Instant.now());
    }

    public static <T> IntegrationEvent<T> create(
            T payload,
            String aggregateId,
            String aggregateType,
            String type,
            int version,
            String correlationId,
            String causationId,
            UUID eventId,
            Instant occurredAt
    ) {
        Objects.requireNonNull(payload, "payload must not be null");
        String resolvedType = firstNonBlank(type, resolveTypeFromAnnotationOrClass(payload));
        String resolvedAggregateType = firstNonBlank(aggregateType, resolveAggregateTypeFromAnnotationOrClass(payload));

        UUID resolvedEventId = eventId == null ? UUID.randomUUID() : eventId;
        Instant resolvedOccurredAt = occurredAt == null ? Instant.now() : occurredAt;

        return new IntegrationEvent<>(
                resolvedEventId,
                resolvedType,
                version,
                resolvedAggregateType,
                aggregateId,
                correlationId,
                causationId,
                resolvedOccurredAt,
                payload
        );
    }

    private static String resolveTypeFromAnnotationOrClass(Object payload) {
        EventMeta meta = payload.getClass().getAnnotation(EventMeta.class);
        if (meta != null && !meta.type().isBlank()) {
            return meta.type();
        }
        String name = payload.getClass().getSimpleName();
        if (name.endsWith("Payload")) {
            name = name.substring(0, name.length() - "Payload".length());
        }
        return name;
    }

    private static String resolveAggregateTypeFromAnnotationOrClass(Object payload) {
        EventMeta meta = payload.getClass().getAnnotation(EventMeta.class);
        if (meta != null && !meta.aggregateType().isBlank()) {
            return meta.aggregateType();
        }
        // Heuristic: if payload class name contains the aggregate (e.g., ClientRegisteredPayload) -> "Client"
        String name = payload.getClass().getSimpleName();
        if (name.contains("Registered") || name.contains("Created") || name.contains("Updated")) {
            // split camel case and take first token
            String first = name.replaceAll("([a-z])([A-Z])", "$1 $2").split(" ")[0];
            if (!first.isBlank()) return first;
        }
        // fallback to payload simple name
        return name;
    }

    private static String firstNonBlank(String primary, String fallback) {
        if (primary != null && !primary.isBlank()) return primary;
        return fallback == null ? "" : fallback;
    }
}
