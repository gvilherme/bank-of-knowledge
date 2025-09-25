package com.gtechnologia.bank.adapters.out.jpa.converter;

import com.gtechnologia.bank.domain.model.DocumentNumber;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class DocumentNumberConverter implements AttributeConverter<DocumentNumber, String> {
    @Override public String convertToDatabaseColumn(DocumentNumber dn) { return dn.number(); }
    @Override public DocumentNumber convertToEntityAttribute(String s) { return new DocumentNumber(s); }
}
