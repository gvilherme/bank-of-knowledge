package com.gtechnologia.bank.config;

import com.gtechnologia.bank.adapters.infrastructure.TxAccountUseCase;
import com.gtechnologia.bank.adapters.infrastructure.TxClientUseCase;
import com.gtechnologia.bank.adapters.out.event.InMemoryEventBroker;
import com.gtechnologia.bank.adapters.out.event.OutboxEventPublisher;
import com.gtechnologia.bank.application.ports.in.ClientUseCase;
import com.gtechnologia.bank.application.ports.out.event.EventBroker;
import com.gtechnologia.bank.application.ports.out.log.LoggerPort;
import com.gtechnologia.bank.application.ports.out.persistence.ClientRepository;
import com.gtechnologia.bank.application.ports.out.persistence.OutboxRepository;
import com.gtechnologia.bank.application.ports.in.AccountUseCase;
import com.gtechnologia.bank.application.ports.out.persistence.AccountRepository;
import com.gtechnologia.bank.application.ports.out.event.EventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    EventBroker eventBroker() {
        return new InMemoryEventBroker();
    }

    @Bean
    EventPublisher eventPublisher(OutboxRepository outboxRepository, EventBroker eventBroker) {
        return new OutboxEventPublisher(outboxRepository, eventBroker);
    }

    @Bean
    AccountUseCase accountUseCase(AccountRepository repo, EventPublisher eventPublisher, LoggerPort logger) {
        return new TxAccountUseCase(repo, eventPublisher, logger);
    }

    @Bean
    ClientUseCase clientUseCase(ClientRepository clientRepository, EventPublisher eventPublisher, LoggerPort logger) {
        return new TxClientUseCase(clientRepository, eventPublisher, logger);
    }
}
