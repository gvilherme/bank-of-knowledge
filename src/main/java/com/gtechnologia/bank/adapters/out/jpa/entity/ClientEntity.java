package com.gtechnologia.bank.adapters.out.jpa.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    public ClientEntity(UUID id, ClientInformationEntity clientInformation) {
        this.id = id;
        this.clientInformation = clientInformation;
    }

    public ClientEntity() {

    }
}
