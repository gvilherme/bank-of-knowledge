package com.gtechnologia.bank.application;

import com.gtechnologia.bank.adapters.out.event.InMemoryEventQueue;
import com.gtechnologia.bank.adapters.out.memory.InMemoryAccountRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    private InMemoryAccountRepository createRepo() {
        return new InMemoryAccountRepository();
    }

    private InMemoryEventQueue createEventQueue() {
        return new InMemoryEventQueue();
    }

    private AccountService createService(InMemoryAccountRepository repo, InMemoryEventQueue queue) {
        return new AccountService(repo, queue);
    }

    @Test
    void shouldOpenAccountSuccessfully() {
        // Arrange
        var repo = createRepo();
        var queue = createEventQueue();
        var service = createService(repo, queue);
        var accountId = UUID.randomUUID();
        var initialDeposit = new BigDecimal("100.00");

        // Act
        var returnedId = service.open(accountId, initialDeposit);

        // Assert
        assertAll(
                () -> assertEquals(accountId, returnedId),
                () -> assertTrue(repo.findById(accountId).isPresent()),
                () -> assertEquals(initialDeposit, repo.findById(accountId).get().balance()),
                () -> assertEquals("Account open with Id: " + accountId, queue.getNextEvent().toString())
        );
    }

    @Test
    void shouldNotOpenDuplicateAccount() {
        // Arrange
        var repo = createRepo();
        var queue = createEventQueue();
        var service = createService(repo, queue);
        var accountId = UUID.randomUUID();

        // Act
        service.open(accountId, BigDecimal.ZERO);

        // Assert
        assertThrows(IllegalStateException.class, () -> service.open(accountId, BigDecimal.ZERO));
    }

    @Test
    void shouldNotOpenDueToOutOfBoundsInitialDeposit() {
        // Arrange
        var repo = createRepo();
        var queue = createEventQueue();
        var service = createService(repo, queue);

        var negativeDepositId = UUID.randomUUID();
        var excessiveDepositId = UUID.randomUUID();

        // Act
        Exception negativeDepositException = assertThrows(IllegalArgumentException.class,
                () -> service.open(negativeDepositId, new BigDecimal("-100.00")));
        Exception excessiveDepositException = assertThrows(IllegalArgumentException.class,
                () -> service.open(excessiveDepositId, new BigDecimal("10000.0000001")));

        // Assert
        assertAll(
                () -> assertNotNull(negativeDepositException),
                () -> assertNotNull(excessiveDepositException)
        );
    }


    @Test
    void shouldThrowExceptionForInvalidDepositAmount() {
        // Arrange
        var repo = createRepo();
        var queue = createEventQueue();
        var service = createService(repo, queue);
        var accountId = UUID.randomUUID();
        service.open(accountId, BigDecimal.ZERO);

        // Act & Assert
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> service.deposit(accountId, new BigDecimal("-100.00"))),
                () -> assertThrows(IllegalArgumentException.class, () -> service.deposit(accountId, new BigDecimal("0.00")))
        );
    }

    @Test
    void shouldThrowExceptionForDepositOnNonexistentAccount() {
        // Arrange
        var repo = createRepo();
        var queue = createEventQueue();
        var service = createService(repo, queue);
        var accountId = UUID.randomUUID();

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> service.deposit(accountId, new BigDecimal("100.00")));
    }

    @Test
    void shouldDepositSuccessfully() {
        // Arrange
        var repo = createRepo();
        var queue = createEventQueue();
        var service = createService(repo, queue);
        var accountId = UUID.randomUUID();
        service.open(accountId, BigDecimal.ZERO);

        // Act
        service.deposit(accountId, new BigDecimal("100.00"));

        // Assert
        assertEquals(new BigDecimal("100.00"), repo.findById(accountId).get().balance());
    }

    @Test
    void shouldThrowExceptionForInvalidWithdrawAmount() {
        // Arrange
        var repo = createRepo();
        var queue = createEventQueue();
        var service = createService(repo, queue);
        var accountId = UUID.randomUUID();
        service.open(accountId, BigDecimal.ZERO);

        // Act & Assert
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> service.withdraw(accountId, new BigDecimal("-100.00"))),
                () -> assertThrows(IllegalArgumentException.class, () -> service.withdraw(accountId, new BigDecimal("0.00"))),
                () -> assertThrows(IllegalStateException.class, () -> service.withdraw(accountId, new BigDecimal("100.00")))
        );
    }

    @Test
    void shouldThrowExceptionForWithdrawOnNonexistentAccount() {
        // Arrange
        var repo = createRepo();
        var queue = createEventQueue();
        var service = createService(repo, queue);
        var accountId = UUID.randomUUID();

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> service.withdraw(accountId, new BigDecimal("100.00")));
    }

    @Test
    void shouldWithdrawSuccessfully() {
        // Arrange
        var repo = createRepo();
        var queue = createEventQueue();
        var service = createService(repo, queue);
        var accountId = UUID.randomUUID();
        var accountId2 = UUID.randomUUID();
        service.open(accountId, new BigDecimal("100.00"));
        service.open(accountId2, new BigDecimal("100.00"));

        // Act
        service.withdraw(accountId, new BigDecimal("100.00"));
        service.withdraw(accountId2, new BigDecimal("99.999999999999999"));

        // Assert
        assertEquals(BigDecimal.ZERO, repo.findById(accountId).get().balance());
        assertNotEquals(BigDecimal.ZERO, repo.findById(accountId2).get().balance());
    }
}
