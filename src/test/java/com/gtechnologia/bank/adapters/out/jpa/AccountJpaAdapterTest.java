package com.gtechnologia.bank.adapters.out.jpa;

import com.gtechnologia.bank.domain.model.Account;
import com.gtechnologia.bank.domain.model.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import(AccountJpaAdapter.class)
public class AccountJpaAdapterTest {
    @Autowired
    AccountJpaAdapter adapter;

    @Test
    void save_and_find() {
        var acc = new Account(UUID.randomUUID(), new Money(new BigDecimal("10.00"), Currency.getInstance("BRL")));
        adapter.save(acc);

        var loaded = adapter.findById(acc.getId()).orElseThrow();
        assertThat(loaded.getBalance().amount()).isEqualByComparingTo("10.00");
    }
}
