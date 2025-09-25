package com.gtechnologia.bank.adapters.out.jpa;

import com.gtechnologia.bank.adapters.out.jpa.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataAccountRepository extends JpaRepository<AccountEntity, UUID> {
}
