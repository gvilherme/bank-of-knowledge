package com.gtechnologia.bank.adapters.out.jpa.converter;

import com.gtechnologia.bank.domain.model.Money;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.math.BigDecimal;
import java.util.Currency;

@Converter
public class MoneyConverter implements AttributeConverter<Money, BigDecimal> {
    @Override public BigDecimal convertToDatabaseColumn(Money m) { return m.amount(); }
    @Override public Money convertToEntityAttribute(BigDecimal v) { return new Money(v, Currency.getInstance("BRL")); }
}