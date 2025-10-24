package com.gtechnologia.bank.adapters.out.event;

import com.gtechnologia.bank.application.ports.out.event.EventBroker;
import com.gtechnologia.bank.contracts.IntegrationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@RequiredArgsConstructor
public class RabbitMqEventBroker implements EventBroker {

    private final RabbitTemplate rabbit;

    @Override
    public void send(IntegrationEvent<?> event) throws Exception {
        String routingKey = event.type() + ".v" + event.version(); // ex: ClientRegistered.v2

        MessagePostProcessor headers = m -> {
            var p = m.getMessageProperties();
            p.setHeader("eventId", event.eventId());
            p.setHeader("type", event.type());
            p.setHeader("version", event.version());
            p.setHeader("aggregateType", event.aggregateType());
            p.setHeader("aggregateId", event.aggregateId());
            p.setHeader("correlationId", event.correlationId());
            p.setHeader("causationId", event.causationId());
            p.setHeader("occurredAt", event.occurredAt().toString());
            return m;
        };

        rabbit.convertAndSend(event.aggregateType().toLowerCase() + ".events", routingKey, event.payload(), headers);
    }
}

