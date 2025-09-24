package com.gtechnologia.bank.adapters.in.web.controller;

import com.gtechnologia.bank.adapters.in.web.dto.request.MoneyRequest;
import com.gtechnologia.bank.adapters.in.web.dto.request.OpenAccountRequest;
import com.gtechnologia.bank.adapters.in.web.dto.request.TransferMoneyRequest;
import com.gtechnologia.bank.adapters.in.web.dto.response.AccountGetResponse;
import com.gtechnologia.bank.domain.model.Account;
import com.gtechnologia.bank.domain.ports.in.AccountUseCase;
import com.gtechnologia.bank.util.WrapAccountMetric;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountUseCase useCase;

    public AccountController(AccountUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    @WrapAccountMetric("opened")
    public ResponseEntity<UUID> open(@Valid @RequestBody OpenAccountRequest req) {
        return ResponseEntity.ok(useCase.openAccount(req.getBalance()));
    }

    @PostMapping("/{id}/deposit")
    @WrapAccountMetric("deposited")
    public ResponseEntity<Void> deposit(@PathVariable("id") UUID id, @Valid @RequestBody MoneyRequest req) {
        useCase.deposit(id, req.toMoney());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/withdraw")
    @WrapAccountMetric("withdrawn")
    public ResponseEntity<Void> withdraw(@PathVariable("id") UUID id, @Valid  @RequestBody MoneyRequest req) {
        useCase.withdraw(id, req.toMoney());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/transfer")
    @WrapAccountMetric("transferred")
    public ResponseEntity<Void> transfer(@PathVariable("id") UUID id, @Valid @RequestBody TransferMoneyRequest req) {
        useCase.transfer(id, req.accountToTransferId(), req.moneyRequest().toMoney());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountGetResponse> getBalance(@PathVariable("id") UUID id) {
        Account account = useCase.getAccount(id);
        return ResponseEntity.ok(new AccountGetResponse(account.getId(), account.getBalance().amount(), account.getBalance().currency()));
    }
}
