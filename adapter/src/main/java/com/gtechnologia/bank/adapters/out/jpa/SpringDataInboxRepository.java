package com.gtechnologia.bank.adapters.out.jpa;

import com.gtechnologia.bank.adapters.out.jpa.entity.InboxEntryEntity;
import com.gtechnologia.bank.adapters.out.jpa.entity.InboxKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataInboxRepository extends JpaRepository<InboxEntryEntity, InboxKey> {
}
