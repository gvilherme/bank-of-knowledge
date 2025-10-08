package com.gtechnologia.bank.application.ports.in;

import com.gtechnologia.bank.domain.model.ClientInformation;

import java.util.UUID;

public interface ClientUseCase {
    UUID register(ClientInformation clientInformation);
}
