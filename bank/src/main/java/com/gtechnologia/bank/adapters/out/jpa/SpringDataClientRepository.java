package com.gtechnologia.bank.adapters.out.jpa;

import com.gtechnologia.bank.adapters.out.jpa.entity.ClientEntity;
import com.gtechnologia.bank.domain.model.client.DocumentNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataClientRepository extends JpaRepository<ClientEntity, UUID> {
    Optional<ClientEntity> findByClientInformation_Document(DocumentNumber document);
}
