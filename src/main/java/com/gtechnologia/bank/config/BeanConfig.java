package com.gtechnologia.bank.config;

import com.gtechnologia.bank.adapters.out.event.InMemoryEventQueue;
import com.gtechnologia.bank.application.service.AccountService;
import com.gtechnologia.bank.domain.ports.in.AccountUseCase;
import com.gtechnologia.bank.domain.ports.out.AccountRepositoryPort;
import com.gtechnologia.bank.domain.ports.out.EventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    EventPublisher eventPublisher() {
        return new InMemoryEventQueue();
    }

    @Bean
    AccountUseCase accountUseCase(AccountRepositoryPort repo, EventPublisher eventPublisher) {
        return new AccountService(repo, eventPublisher);
    }
}
