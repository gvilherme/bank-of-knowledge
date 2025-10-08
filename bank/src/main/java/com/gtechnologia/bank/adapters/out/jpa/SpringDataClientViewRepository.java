package com.gtechnologia.bank.adapters.out.jpa;

import com.gtechnologia.bank.adapters.out.jpa.projection.ClientView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataClientViewRepository extends JpaRepository<ClientView, UUID> {
}
