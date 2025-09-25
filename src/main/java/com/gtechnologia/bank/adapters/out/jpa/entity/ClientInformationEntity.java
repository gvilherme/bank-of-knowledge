package com.gtechnologia.bank.adapters.out.jpa.entity;

import com.gtechnologia.bank.adapters.out.jpa.converter.DocumentNumberConverter;
import com.gtechnologia.bank.domain.model.DocumentNumber;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@Table(name = "client_information", schema = "clients")
public class ClientInformationEntity {
    @Column(nullable = false, name = "first_name")
    private String firstName;

    @Column(nullable = false, name = "last_name")
    private String lastName;

    @Convert(converter = DocumentNumberConverter.class)
    @Column(nullable = false, unique = true, name = "document_number")
    private DocumentNumber document;
}
