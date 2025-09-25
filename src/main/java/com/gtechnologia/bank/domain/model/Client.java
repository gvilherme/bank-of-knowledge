package com.gtechnologia.bank.domain.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public final class Client {
    private final UUID id;
    private final ClientInformation clientInformation;

    public Client(UUID id, ClientInformation clientInformation) {
        this.id = id;
        this.clientInformation = clientInformation;
    }
}
