package com.gtechnologia.bank.adapters.in.amqp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtechnologia.bank.adapters.out.jpa.SpringDataClientViewRepository;
import com.gtechnologia.bank.adapters.out.jpa.projection.ClientView;
import com.gtechnologia.bank.adapters.out.service.InboxAdapter;
import com.gtechnologia.bank.contracts.ClientEvent;
import com.gtechnologia.bank.domain.model.client.KycStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ClientRegisteredConsumer {

    private static final String CONSUMER = "account-client-registered";
    private final ObjectMapper mapper;
    private final SpringDataClientViewRepository viewRepo;
    private final InboxAdapter inbox;

    // headers esperados no publish: eventId, type, version, correlationId, aggregateId
    @Transactional
    @RabbitListener(queues = "${bank.mq.queues.accountClient}")
    public void onMessage(ClientEvent event,
                          @Header(name="eventId") String eventId,
                          @Header(name="aggregateId") String aggregateId,
                          @Header(name="type", required=false) String type,
                          @Header(name="version", required=false) Integer version,
                          @Header(name="correlationId", required=false) String correlationId) throws Exception {

        if (!inbox.tryStart(eventId, CONSUMER)) {
            // duplicado → idempotência
            // métrica: consumer_dedupe_total++
            return;
        }
        UUID clientId = UUID.fromString(aggregateId); // usamos aggregateId do evento como clientId

        var view = viewRepo.findById(clientId)
                .orElseGet(() -> {
                    var v = new ClientView();
                    v.setClientId(clientId);
                    v.setKycStatus(KycStatus.PENDING);
                    v.setCreatedAt(Instant.now());
                    return v;
                });

        // apenas registra lastEventId/updatedAt no “registered”; status segue UNKNOWN
        view.setLastEventId(eventId);
        view.setUpdatedAt(Instant.now());
        viewRepo.save(view);

        inbox.markProcessed(eventId, CONSUMER);
        // tracing: propagar correlationId em logs
    }
}
