package com.gtechnologia.bank.adapters.out.jpa.entity;

import com.gtechnologia.bank.domain.model.client.KycStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
@Getter
@Table(name = "clients", schema = "clients")
public class ClientEntity {
    @Id
    private UUID id;
    @Embedded
    private ClientInformationEntity clientInformation;
    @Enumerated(EnumType.STRING)
    private KycStatus kycStatus;

    public ClientEntity(UUID id, ClientInformationEntity clientInformation, KycStatus kycStatus) {
        this.id = id;
        this.clientInformation = clientInformation;
        this.kycStatus = kycStatus;
    }

    public ClientEntity() {

    }
}
