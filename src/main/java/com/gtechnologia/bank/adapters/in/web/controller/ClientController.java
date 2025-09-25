package com.gtechnologia.bank.adapters.in.web.controller;

import com.gtechnologia.bank.application.ports.in.ClientUseCase;
import com.gtechnologia.bank.domain.model.ClientInformation;
import com.gtechnologia.bank.domain.model.DocumentNumber;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clients")
public class ClientController {
    private final ClientUseCase clientUseCase;

    public ClientController(ClientUseCase clientUseCase) {
        this.clientUseCase = clientUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> addClient() {
        clientUseCase.register(new ClientInformation("Apenas","Um teste", new DocumentNumber("12345678900")));
        return ResponseEntity.created(null).build();
    }
}
