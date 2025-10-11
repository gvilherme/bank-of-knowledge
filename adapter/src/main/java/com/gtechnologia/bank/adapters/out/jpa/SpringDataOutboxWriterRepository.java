package com.gtechnologia.bank.adapters.out.jpa;

import com.gtechnologia.bank.adapters.out.jpa.entity.IntegrationEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface SpringDataOutboxWriterRepository extends JpaRepository<IntegrationEventEntity, UUID>,
        JpaSpecificationExecutor<IntegrationEventEntity> {
}
