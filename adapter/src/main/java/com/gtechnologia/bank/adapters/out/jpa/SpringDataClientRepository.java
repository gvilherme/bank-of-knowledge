package com.gtechnologia.bank.adapters.out.jpa;

import com.gtechnologia.bank.adapters.out.jpa.entity.ClientEntity;
import com.gtechnologia.bank.domain.model.client.ClientInformation;
import com.gtechnologia.bank.domain.model.client.DocumentNumber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataClientRepository extends JpaRepository<ClientEntity, UUID> {
    Optional<ClientEntity> findByClientInformation_Document(DocumentNumber document);

    Page<ClientEntity> findAll(Pageable pageable);

    Page<ClientEntity> findByClientInformation_Document(DocumentNumber document, Pageable pageable);
}
