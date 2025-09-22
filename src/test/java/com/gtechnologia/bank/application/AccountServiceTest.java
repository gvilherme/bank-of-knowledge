package com.gtechnologia.bank.application;

import com.gtechnologia.bank.adapters.out.event.InMemoryEventQueue;
import com.gtechnologia.bank.adapters.out.memory.InMemoryAccountRepository;
import com.gtechnologia.bank.domain.ports.in.OpenAccountUseCase;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {
    @Test
    void open_account_success() {
        var repo = new InMemoryAccountRepository();
        var queue = new InMemoryEventQueue();
        OpenAccountUseCase useCase = new AccountService(repo, queue);

        var id = useCase.open("ACC-1", new BigDecimal("100.00"));

        assertEquals("ACC-1", id);
        assertTrue(repo.findById("ACC-1").isPresent());
        assertEquals(new BigDecimal("100.00"), repo.findById("ACC-1").get().balance());
        assertEquals("Account open with Id: ACC-1", queue.getNextEvent().toString());
    }

    @Test
    void cannot_open_duplicate() {
        var repo = new InMemoryAccountRepository();
        var svc = new AccountService(repo, new InMemoryEventQueue());
        svc.open("ACC-1", BigDecimal.ZERO);

        assertThrows(IllegalStateException.class, () -> svc.open("ACC-1", BigDecimal.ZERO));
    }

    @Test
    void cannot_open_boundary_exception() {
        var repo = new InMemoryAccountRepository();
        var svc = new AccountService(repo, new InMemoryEventQueue());
        var id = svc.open("ACC-1", new BigDecimal("10000.00"));
        assertEquals("ACC-1", id);
        assertThrows(IllegalArgumentException.class, () -> svc.open("ACC-2", new  BigDecimal("10000.00000001")));
    }

    @Test
    void valid_deposit() {
        var repo = new InMemoryAccountRepository();
        var svc = new AccountService(repo, new InMemoryEventQueue());
        svc.open("ACC-1", BigDecimal.ZERO);

        svc.deposit("ACC-1", new BigDecimal("100.00"));
        assertEquals(new BigDecimal("100.00"), repo.findById("ACC-1").get().balance());
    }

    @Test
    void invalid_deposit_no_such_account() {
        var repo = new InMemoryAccountRepository();
        var svc = new AccountService(repo, new InMemoryEventQueue());

        assertThrows(IllegalStateException.class, () -> svc.deposit("ACC-1", new BigDecimal("100.00")));
    }

    @Test
    void invalid_deposit_negative_or_zero_amount() {
        var repo = new InMemoryAccountRepository();
        var svc = new AccountService(repo, new InMemoryEventQueue());
        svc.open("ACC-1", BigDecimal.ZERO);

        assertThrows(IllegalArgumentException.class, () -> svc.deposit("ACC-1", new BigDecimal("-100.00")));
        assertThrows(IllegalArgumentException.class, () -> svc.deposit("ACC-1", new BigDecimal("0.00")));
    }
}
