package com.gtechnologia.bank.adapters.in.web.controller;

import com.gtechnologia.bank.adapters.in.web.dto.request.ClientRegisterRequest;
import com.gtechnologia.bank.adapters.in.web.dto.response.ClientResponse;
import com.gtechnologia.bank.adapters.in.web.dto.response.DataResponse;
import com.gtechnologia.bank.application.ports.in.ClientUseCase;
import com.gtechnologia.bank.domain.model.client.ClientInformation;
import com.gtechnologia.bank.domain.model.client.DocumentNumber;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clients")
public class ClientController {
    private final ClientUseCase clientUseCase;

    public ClientController(ClientUseCase clientUseCase) {
        this.clientUseCase = clientUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> addClient(@Valid@RequestBody ClientRegisterRequest clientRegisterRequest) {
        var id = clientUseCase.register(new ClientInformation(clientRegisterRequest.firstName(), clientRegisterRequest.lastName(), clientRegisterRequest.getDocumentNumber()));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<Page<ClientResponse>> listClients(@PageableDefault(size = 20) Pageable pageable) {
        var clients = clientUseCase.getClients(pageable.getPageNumber(), pageable.getPageSize());
        List<ClientResponse> dtos = clients.stream()
                .map(ClientResponse::fromDomain)
                .collect(Collectors.toList());

        Page<ClientResponse> page = toPage(dtos, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<ClientResponse>> getClient(@PathVariable UUID id) {
        var client = clientUseCase.getClientById(id);
        var clientResponse = ClientResponse.fromDomain(client);
        return ResponseEntity.ok(new DataResponse<>(clientResponse));
    }

    private static <T> Page<T> toPage(List<T> list, Pageable pageable) {
        int total = list.size();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), total);
        List<T> content = start >= total ? List.of() : list.subList(start, end);
        return new PageImpl<>(content, pageable, total);
    }
}
