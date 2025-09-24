package com.gtechnologia.bank.domain.ports.in;

import com.gtechnologia.bank.domain.exception.account.AccountDoesNotExistsException;
import com.gtechnologia.bank.domain.exception.account.AccountException;
import com.gtechnologia.bank.domain.exception.account.InsufficientBalanceException;
import com.gtechnologia.bank.domain.exception.account.InvalidAmountException;
import com.gtechnologia.bank.domain.model.Account;
import com.gtechnologia.bank.domain.model.Money;
import com.gtechnologia.bank.domain.ports.out.AccountRepositoryPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.Currency;
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

    private Money moneyHelper(String amount) {
        return new Money(new BigDecimal(amount), Currency.getInstance("BRL"));
    }

    private Money moneyHelper(BigDecimal amount) {
        return new Money(amount, Currency.getInstance("BRL"));
    }

    @Test
    void shouldOpenAccountAccountSuccessfully() {
        // Arrange & Act
        Money initialDeposit = moneyHelper("100.00");
        var returnedId = accountUseCase.openAccount(initialDeposit);
        var account = new Account(returnedId, moneyHelper("100.00"));
        when(repo.findById(returnedId)).thenReturn(Optional.of(account));

        // Assert
        assertAll(
                () -> assertInstanceOf(UUID.class, returnedId),
                () -> assertTrue(repo.findById(returnedId).isPresent()),
                () -> assertEquals(initialDeposit, repo.findById(returnedId).get().getBalance())
        );
    }

    @Test
    void shouldNotOpenAccountDueToOutOfBoundsInitialDeposit() {
        // Arrange
        final Money INITIAL_DEPOSIT_LOWER_BOUNDS = moneyHelper("-000.000000000000000001");
        final Money INITIAL_DEPOSIT_UPPER_BOUNDS = moneyHelper("10000.0000001");

        // Act
        Exception negativeDepositException = assertThrows(InvalidAmountException.class,
                () -> accountUseCase.openAccount(INITIAL_DEPOSIT_LOWER_BOUNDS));
        Exception excessiveDepositException = assertThrows(InvalidAmountException.class,
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
        var accountId = accountUseCase.openAccount(moneyHelper(BigDecimal.ZERO));
        var account = new Account(accountId, moneyHelper(BigDecimal.ZERO));
        when(repo.findById(accountId)).thenReturn(Optional.of(account));


        // Act & Assert
        assertAll(
                () -> assertThrows(InvalidAmountException.class, () -> accountUseCase.deposit(accountId, moneyHelper("-100.00"))),
                () -> assertThrows(InvalidAmountException.class, () -> accountUseCase.deposit(accountId, moneyHelper("0.00")))
        );
    }

    @Test
    void shouldThrowExceptionForDepositOnNonexistentAccount() {
        // Arrange
        var accountId = UUID.randomUUID();

        // Act & Assert
        assertThrows(AccountDoesNotExistsException.class, () -> accountUseCase.deposit(accountId, moneyHelper("100.00")));
    }

    @Test
    void shouldDepositSuccessfully() {
        // Arrange
        var accountId = accountUseCase.openAccount(moneyHelper(BigDecimal.ZERO));
        var account = new Account(accountId, moneyHelper(BigDecimal.ZERO));
        when(repo.findById(accountId)).thenReturn(Optional.of(account));

        // Act
        accountUseCase.deposit(accountId, moneyHelper("100.00"));

        // Assert
        assertEquals(moneyHelper("100.00"), repo.findById(accountId).get().getBalance());
    }

    @Test
    void shouldThrowExceptionForInvalidWithdrawAmount() {
        // Arrange
        var accountId = accountUseCase.openAccount(moneyHelper(BigDecimal.ZERO));
        var account = new Account(accountId, moneyHelper(BigDecimal.ZERO));
        when(repo.findById(accountId)).thenReturn(Optional.of(account));

        // Act & Assert
        assertAll(
                () -> assertThrows(InvalidAmountException.class, () -> accountUseCase.withdraw(accountId, moneyHelper("-100.00"))),
                () -> assertThrows(InvalidAmountException.class, () -> accountUseCase.withdraw(accountId, moneyHelper("0.00"))),
                () -> assertThrows(InsufficientBalanceException.class, () -> accountUseCase.withdraw(accountId, moneyHelper("100.00")))
        );
    }

    @Test
    void shouldThrowExceptionForWithdrawOnNonexistentAccount() {
        // Arrange
        var accountId = UUID.randomUUID();

        // Act & Assert
        assertThrows(AccountDoesNotExistsException.class, () -> accountUseCase.withdraw(accountId, moneyHelper("100.00")));
    }

    @Test
    void shouldWithdrawSuccessfully() {
        // Arrange
        var accountId = accountUseCase.openAccount(moneyHelper("100.00"));
        var account = new Account(accountId, moneyHelper("100.00"));
        var accountId2 = accountUseCase.openAccount(moneyHelper("100.00"));
        var account2 = new Account(accountId, moneyHelper("100.00"));
        when(repo.findById(accountId)).thenReturn(Optional.of(account));
        when(repo.findById(accountId2)).thenReturn(Optional.of(account2));

        // Act
        accountUseCase.withdraw(accountId, moneyHelper("100.00"));
        accountUseCase.withdraw(accountId2, moneyHelper("99.999999999999999"));

        // Assert
        assertEquals(BigDecimal.ZERO, repo.findById(accountId).get().getBalance().amount());
        assertNotEquals(BigDecimal.ZERO, repo.findById(accountId2).get().getBalance().amount());
    }
}
