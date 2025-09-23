package com.gtechnologia.bank.adapters.out.jpa;

import com.gtechnologia.bank.domain.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import(AccountJpaAdapter.class)
public class AccountJpaAdapterTest {
    @Autowired
    AccountJpaAdapter adapter;

    @Test
    void save_and_find() {
        var acc = new Account(UUID.randomUUID(), new BigDecimal("10.00"));
        adapter.save(acc);

        var loaded = adapter.findById(acc.id()).orElseThrow();
        assertThat(loaded.balance()).isEqualByComparingTo("10.00");
    }
}
