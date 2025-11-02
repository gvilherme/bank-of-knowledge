package com.gtechnologia.bank.adapters.in.web.dto.request;

import com.gtechnologia.bank.domain.model.client.DocumentNumber;
import jakarta.validation.constraints.NotNull;

public record ClientRegisterRequest(
        @NotNull String firstName,
        @NotNull String lastName,
        @NotNull String documentNumber
) {
    public DocumentNumber getDocumentNumber() {
        return new DocumentNumber(documentNumber);
    }
}
