package com.gtechnologia.bank.adapters.out.service;

import com.gtechnologia.bank.adapters.out.jpa.SpringDataInboxRepository;
import com.gtechnologia.bank.adapters.out.jpa.entity.InboxEntryEntity;
import com.gtechnologia.bank.adapters.out.jpa.entity.InboxKey;
import com.gtechnologia.bank.application.ports.out.persistence.InboxRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InboxAdapter implements InboxRepository {
    private final SpringDataInboxRepository repo;

    public InboxAdapter(SpringDataInboxRepository repo) {
        this.repo = repo;
    }

    @Transactional
    @Override
    public boolean tryStart(String eventId, String consumer){
        var key = new InboxKey(consumer, eventId);
        if (repo.existsById(key)) return false;        // duplicado
        repo.save(new InboxEntryEntity(key));                // reservou
        return true;
    }

    @Transactional
    @Override
    public void markProcessed(String eventId, String consumer){
        var key = new InboxKey(consumer, eventId);
        repo.findById(key).ifPresent(InboxEntryEntity::markProcessed);
    }
}
