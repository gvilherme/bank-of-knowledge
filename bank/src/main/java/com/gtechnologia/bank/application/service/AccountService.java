package com.gtechnologia.bank.application.service;

import com.gtechnologia.bank.domain.exception.account.AccountDoesNotExistsException;
import com.gtechnologia.bank.domain.model.account.Account;
import com.gtechnologia.bank.domain.model.account.AccountStatus;
import com.gtechnologia.bank.domain.model.account.Money;
import com.gtechnologia.bank.application.ports.in.AccountUseCase;
import com.gtechnologia.bank.application.ports.out.persistence.AccountRepository;
import com.gtechnologia.bank.application.ports.out.event.EventPublisher;
import com.gtechnologia.bank.util.WrapAccountException;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class AccountService implements AccountUseCase {
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository repo;
    private final EventPublisher publisher;

    public AccountService(AccountRepository repo, EventPublisher publisher) {
        this.repo = repo;
        this.publisher = publisher;
    }

    @WrapAccountException
    @Override
    @Transactional
    public UUID openAccount(Money initialDeposit, UUID clientId) {
        logger.info("Opening account");
        var acc = new Account(UUID.randomUUID(), clientId, initialDeposit, AccountStatus.PENDING);
        repo.save(acc);
        logger.info("Account open with Id: {}", acc.getId());
        return acc.getId();
    }

    @WrapAccountException
    @Override
    @Transactional
    public void withdraw(UUID id, Money amount) {
        logger.info("Withdrawing account from account with Id: {}", id);
        var acc = repo.findById(id).orElseThrow(() -> new AccountDoesNotExistsException("no such account", id));
        acc.withdraw(amount);
        repo.save(acc);
        logger.info("Withdrawn account from account with Id: {}", id);
    }

    @WrapAccountException
    @Override
    @Transactional
    public void deposit(UUID id, Money amount) {
        logger.info("Depositing account to account with Id: {}", id);
        var acc = repo.findById(id).orElseThrow(() -> new AccountDoesNotExistsException("no such account", id));
        acc.deposit(amount);
        repo.save(acc);
        logger.info("Deposited account to account with Id: {}", id);
    }

    @WrapAccountException
    @Override
    @Transactional(readOnly = true)
    public Account getAccount(UUID id) {
        logger.info("Getting account info with Id: " + id);
        var acc = repo.findById(id).orElseThrow(() -> new AccountDoesNotExistsException("no such account", id));
        logger.info("Got account with Id: {}", id);
        return acc;
    }

    @WrapAccountException
    @Override
    @Transactional
    public void transfer(UUID fromAccountId, UUID toAccountId, Money amount) {
        logger.info("Transferring balance from account with Id: {}", fromAccountId);
        var acc = repo.findById(fromAccountId).orElseThrow(() -> new AccountDoesNotExistsException("no such account", fromAccountId));
        var to = repo.findById(toAccountId).orElseThrow(() -> new AccountDoesNotExistsException("no such account", toAccountId));
        acc.transferTo(to, amount);
        repo.save(acc);
        repo.save(to);
        logger.info("Transferred balance from account with Id: {}", fromAccountId);
    }

}
