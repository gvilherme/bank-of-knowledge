package com.gtechnologia.bank.contracts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gtechnologia.bank.domain.model.client.ClientInformation;
import com.gtechnologia.bank.domain.model.client.KycStatus;
import lombok.Getter;

import java.util.UUID;

@Getter
public final class ClientEvent {
    private final UUID clientId;
    private final ClientInformation clientInformation;
    private final KycStatus kycStatus;

    @JsonCreator
    public ClientEvent(
            @JsonProperty("clientId") UUID clientId,
            @JsonProperty("clientInformation") ClientInformation clientInformation,
            @JsonProperty("kycStatus") KycStatus kycStatus
    ) {
        this.clientId = clientId;
        this.clientInformation = clientInformation;
        this.kycStatus = kycStatus;
    }
}
