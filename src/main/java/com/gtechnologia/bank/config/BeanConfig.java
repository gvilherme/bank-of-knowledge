package com.gtechnologia.bank.config;

import com.gtechnologia.bank.adapters.out.event.InMemoryEventQueue;
import com.gtechnologia.bank.application.service.AccountService;
import com.gtechnologia.bank.domain.ports.in.AccountUseCase;
import com.gtechnologia.bank.domain.ports.out.AccountRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    AccountUseCase accountUseCase(AccountRepositoryPort repo) {
        return new AccountService(repo, new InMemoryEventQueue());
    }
}
