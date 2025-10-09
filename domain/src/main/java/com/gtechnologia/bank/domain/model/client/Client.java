package com.gtechnologia.bank.domain.model.client;

import lombok.Getter;

import java.util.UUID;

@Getter
public final class Client {
    private final UUID clientId;
    private final ClientInformation clientInformation;
    private KycStatus kycStatus;

    public Client(UUID clientId, ClientInformation clientInformation) {
        this.clientId = clientId;
        this.clientInformation = clientInformation;
        this.kycStatus = KycStatus.PENDING;
    }

    public Client(UUID clientId, ClientInformation clientInformation, KycStatus kycStatus) {
        this.kycStatus = kycStatus;
        this.clientInformation = clientInformation;
        this.clientId = clientId;
    }

    public void approveKyc() {
        if (kycStatus == KycStatus.PENDING) {
            this.kycStatus = KycStatus.APPROVED;
        } else throw new IllegalStateException("Kyc status is already a final status: " + kycStatus);
    }

    public void rejectKyc() {
        if (kycStatus == KycStatus.PENDING) {
            this.kycStatus = KycStatus.REJECTED;
        } else throw new IllegalStateException("Kyc status is already a final status: " + kycStatus);
    }
}
