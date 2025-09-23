package com.gtechnologia.bank.adapters.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtechnologia.bank.adapters.in.web.dto.request.MoneyRequest;
import com.gtechnologia.bank.adapters.in.web.dto.request.OpenAccountRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired ObjectMapper om;

    @Test
    void open_deposit_withdraw_happy_path() throws Exception {
        // open account
        var res = mvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new OpenAccountRequest(new BigDecimal("100.00"), null))))
                .andExpect(status().isOk())
                .andReturn();
        var id = UUID.fromString(res.getResponse().getContentAsString().replace("\"",""));

        // deposits
        mvc.perform(post("/accounts/{id}/deposit", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new MoneyRequest(new BigDecimal("50.00"), null))))
                .andExpect(status().isNoContent());

        // withdraws
        mvc.perform(post("/accounts/{id}/withdraw", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new MoneyRequest(new BigDecimal("80.00"), null))))
                .andExpect(status().isNoContent());

        mvc.perform(get("/accounts/{id}", id))
                .andExpect(result -> {
                    var expected = "{\"accountId\":\""+id+"\",\"balance\":70.00,\"currency\":\"BRL\"}";
                    var actual = result.getResponse().getContentAsString();
                    if (!expected.equals(actual)) {
                        throw new AssertionError("Expected: " + expected + " but got: " + actual);
                    }
                })
                .andExpect(status().isOk());
    }
}