package com.gtechnologia.bank.domain.ports.in;

import com.gtechnologia.bank.domain.model.Account;
import com.gtechnologia.bank.domain.ports.out.AccountRepositoryPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class AccountUseCaseTest {

    @MockitoBean
    private AccountRepositoryPort repo;
    @Autowired
    private AccountUseCase accountUseCase;


    @Test
    void shouldOpenAccountAccountSuccessfully() {
        // Arrange & Act
        var initialDeposit = new BigDecimal("100.00");
        var returnedId = accountUseCase.openAccount(initialDeposit);
        var account = new Account(returnedId, new BigDecimal("100.00"));
        when(repo.findById(returnedId)).thenReturn(Optional.of(account));

        // Assert
        assertAll(
                () -> assertInstanceOf(UUID.class, returnedId),
                () -> assertTrue(repo.findById(returnedId).isPresent()),
                () -> assertEquals(initialDeposit, repo.findById(returnedId).get().balance())
        );
    }

    @Test
    void shouldNotOpenAccountDueToOutOfBoundsInitialDeposit() {
        // Arrange
        final BigDecimal INITIAL_DEPOSIT_LOWER_BOUNDS = new BigDecimal("-000.000000000000000001");
        final BigDecimal INITIAL_DEPOSIT_UPPER_BOUNDS = new BigDecimal("10000.0000001");

        // Act
        Exception negativeDepositException = assertThrows(IllegalArgumentException.class,
                () -> accountUseCase.openAccount(INITIAL_DEPOSIT_LOWER_BOUNDS));
        Exception excessiveDepositException = assertThrows(IllegalArgumentException.class,
                () -> accountUseCase.openAccount(INITIAL_DEPOSIT_UPPER_BOUNDS));

        // Assert
        assertAll(
                () -> assertNotNull(negativeDepositException),
                () -> assertNotNull(excessiveDepositException)
        );
    }


    @Test
    void shouldThrowExceptionForInvalidDepositAmount() {
        // Arrange
        var accountId = accountUseCase.openAccount(BigDecimal.ZERO);
        var account = new Account(accountId, BigDecimal.ZERO);
        when(repo.findById(accountId)).thenReturn(Optional.of(account));


        // Act & Assert
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> accountUseCase.deposit(accountId, new BigDecimal("-100.00"))),
                () -> assertThrows(IllegalArgumentException.class, () -> accountUseCase.deposit(accountId, new BigDecimal("0.00")))
        );
    }

    @Test
    void shouldThrowExceptionForDepositOnNonexistentAccount() {
        // Arrange
        var accountId = UUID.randomUUID();

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> accountUseCase.deposit(accountId, new BigDecimal("100.00")));
    }

    @Test
    void shouldDepositSuccessfully() {
        // Arrange
        var accountId = accountUseCase.openAccount(BigDecimal.ZERO);
        var account = new Account(accountId, BigDecimal.ZERO);
        when(repo.findById(accountId)).thenReturn(Optional.of(account));

        // Act
        accountUseCase.deposit(accountId, new BigDecimal("100.00"));

        // Assert
        assertEquals(new BigDecimal("100.00"), repo.findById(accountId).get().balance());
    }

    @Test
    void shouldThrowExceptionForInvalidWithdrawAmount() {
        // Arrange
        var accountId = accountUseCase.openAccount(BigDecimal.ZERO);
        var account = new Account(accountId, BigDecimal.ZERO);
        when(repo.findById(accountId)).thenReturn(Optional.of(account));

        // Act & Assert
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> accountUseCase.withdraw(accountId, new BigDecimal("-100.00"))),
                () -> assertThrows(IllegalArgumentException.class, () -> accountUseCase.withdraw(accountId, new BigDecimal("0.00"))),
                () -> assertThrows(IllegalStateException.class, () -> accountUseCase.withdraw(accountId, new BigDecimal("100.00")))
        );
    }

    @Test
    void shouldThrowExceptionForWithdrawOnNonexistentAccount() {
        // Arrange
        var accountId = UUID.randomUUID();

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> accountUseCase.withdraw(accountId, new BigDecimal("100.00")));
    }

    @Test
    void shouldWithdrawSuccessfully() {
        // Arrange
        var accountId = accountUseCase.openAccount(new BigDecimal("100.00"));
        var account = new Account(accountId, new BigDecimal("100.00"));
        var accountId2 = accountUseCase.openAccount(new BigDecimal("100.00"));
        var account2 = new Account(accountId, new BigDecimal("100.00"));
        when(repo.findById(accountId)).thenReturn(Optional.of(account));
        when(repo.findById(accountId2)).thenReturn(Optional.of(account2));

        // Act
        accountUseCase.withdraw(accountId, new BigDecimal("100.00"));
        accountUseCase.withdraw(accountId2, new BigDecimal("99.999999999999999"));

        // Assert
        assertEquals(BigDecimal.ZERO, repo.findById(accountId).get().balance());
        assertNotEquals(BigDecimal.ZERO, repo.findById(accountId2).get().balance());
    }
}
