package com.gtechnologia.bank.adapters.in.web.dto.response;

import java.util.UUID;

public record ClientResponse(UUID id, String firstName, String lastName, String documentNumber) {
    public static ClientResponse fromDomain(com.gtechnologia.bank.domain.model.client.Client client) {
        return new ClientResponse(client.getClientId(), client.getClientInformation().firstName(), client.getClientInformation().lastName(), client.getClientInformation().document().number());
    }
}
