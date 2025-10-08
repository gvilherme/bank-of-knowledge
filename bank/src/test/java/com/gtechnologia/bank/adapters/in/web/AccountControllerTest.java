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

    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    private static final String CORRELATION_ID_VALUE = "test-correlation-id";

    private <T extends org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder> T withCorrelationId(T builder) {
        return (T) builder.header(CORRELATION_ID_HEADER, CORRELATION_ID_VALUE);
    }

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper om;

    @Test
    void open_deposit_withdraw_happy_path() throws Exception {
        // open account
        var res = mvc.perform(withCorrelationId(post("/accounts"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new OpenAccountRequest(new BigDecimal("100.00"), null))))
                .andExpect(status().isOk())
                .andReturn();
        var id = UUID.fromString(res.getResponse().getContentAsString().replace("\"", ""));

        // deposits
        mvc.perform(withCorrelationId(post("/accounts/{id}/deposit", id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new MoneyRequest(new BigDecimal("50.00"), null))))
                .andExpect(status().isNoContent());

        // withdraws
        mvc.perform(withCorrelationId(post("/accounts/{id}/withdraw", id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new MoneyRequest(new BigDecimal("80.00"), null))))
                .andExpect(status().isNoContent());

        // get account info
        mvc.perform(withCorrelationId(get("/accounts/{id}", id)))
                .andExpect(result -> {
                    var expected = "{\"accountId\":\"" + id + "\",\"balance\":70.00,\"currency\":\"BRL\"}";
                    var actual = result.getResponse().getContentAsString();
                    if (!expected.equals(actual)) {
                        throw new AssertionError("Expected: " + expected + " but got: " + actual);
                    }
                })
                .andExpect(status().isOk());
    }

    @Test
    void open_account_with_invalid_initial_deposit() throws Exception {
        // negative initial deposit
        mvc.perform(withCorrelationId(post("/accounts"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new OpenAccountRequest(new BigDecimal("-0.01"), null))))
                .andExpect(status().isBadRequest());

        // excessive initial deposit
        mvc.perform(withCorrelationId(post("/accounts"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new OpenAccountRequest(new BigDecimal("10000.01"), null))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deposit_with_invalid_amount() throws Exception {
        // open account
        var res = mvc.perform(withCorrelationId(post("/accounts"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new OpenAccountRequest(new BigDecimal("0.00"), null))))
                .andExpect(status().isOk())
                .andReturn();
        var id = UUID.fromString(res.getResponse().getContentAsString().replace("\"", ""));

        // zero deposit
        mvc.perform(withCorrelationId(post("/accounts/{id}/deposit", id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new MoneyRequest(new BigDecimal("0.00"), null))))
                .andExpect(status().isBadRequest());

        // negative deposit
        mvc.perform(withCorrelationId(post("/accounts/{id}/deposit", id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new MoneyRequest(new BigDecimal("-0.01"), null))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void withdraw_with_invalid_amount_or_insufficient_balance() throws Exception {
        // open account
        var res = mvc.perform(withCorrelationId(post("/accounts"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new OpenAccountRequest(new BigDecimal("100.00"), null))))
                .andExpect(status().isOk())
                .andReturn();
        var id = UUID.fromString(res.getResponse().getContentAsString().replace("\"", ""));

        // zero withdraw
        mvc.perform(withCorrelationId(post("/accounts/{id}/withdraw", id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new MoneyRequest(new BigDecimal("0.00"), null))))
                .andExpect(status().isBadRequest());

        // negative withdraw
        mvc.perform(withCorrelationId(post("/accounts/{id}/withdraw", id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new MoneyRequest(new BigDecimal("-0.01"), null))))
                .andExpect(status().isBadRequest());

        // excessive withdraw
        mvc.perform(withCorrelationId(post("/accounts/{id}/withdraw", id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new MoneyRequest(new BigDecimal("100.01"), null))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void withdraw_with_valid_amount() throws Exception {
        // open account
        var res = mvc.perform(withCorrelationId(post("/accounts"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new OpenAccountRequest(new BigDecimal("100.00"), null))))
                .andExpect(status().isOk())
                .andReturn();
        var id = UUID.fromString(res.getResponse().getContentAsString().replace("\"", ""));

        // valid withdraw
        mvc.perform(withCorrelationId(post("/accounts/{id}/withdraw", id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new MoneyRequest(new BigDecimal("99.99"), null))))
                .andExpect(status().isNoContent());

        // get account info
        mvc.perform(withCorrelationId(get("/accounts/{id}", id)))
                .andExpect(result -> {
                    var expected = "{\"accountId\":\"" + id + "\",\"balance\":0.01,\"currency\":\"BRL\"}";
                    var actual = result.getResponse().getContentAsString();
                    if (!expected.equals(actual)) {
                        throw new AssertionError("Expected: " + expected + " but got: " + actual);
                    }
                })
                .andExpect(status().isOk());
    }

    @Test
    void get_nonexistent_account() throws Exception {
        mvc.perform(withCorrelationId(get("/accounts/{id}", UUID.randomUUID())))
                .andExpect(status().isNotFound());
    }

    @Test
    void deposit_to_nonexistent_account() throws Exception {
        mvc.perform(withCorrelationId(post("/accounts/{id}/deposit", UUID.randomUUID()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new MoneyRequest(new BigDecimal("100.00"), null))))
                .andExpect(status().isNotFound());
    }

    @Test
    void withdraw_to_nonexistent_account() throws Exception {
        mvc.perform(withCorrelationId(post("/accounts/{id}/withdraw", UUID.randomUUID()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new MoneyRequest(new BigDecimal("100.00"), null))))
                .andExpect(status().isNotFound());
    }

    @Test
    void open_account_with_valid_amount_and_executes_transfer() throws Exception {
        // open account 1
        var res1 = mvc.perform(withCorrelationId(post("/accounts"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new OpenAccountRequest(new BigDecimal("500.00"), null))))
                .andExpect(status().isOk())
                .andReturn();
        var id1 = UUID.fromString(res1.getResponse().getContentAsString().replace("\"", ""));

        // open account 2
        var res2 = mvc.perform(withCorrelationId(post("/accounts"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new OpenAccountRequest(new BigDecimal("300.00"), null))))
                .andExpect(status().isOk())
                .andReturn();
        var id2 = UUID.fromString(res2.getResponse().getContentAsString().replace("\"", ""));

        // transfer from account 1 to account 2
        mvc.perform(withCorrelationId(post("/accounts/{id}/transfer", id1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "accountToTransferId": "%s",
                                  "moneyRequest": {
                                    "amount": 200.00,
                                    "currency": "BRL"
                                  }
                                }
                                """.formatted(id2)))
                .andExpect(status().isNoContent());

        // get account 1 info
        mvc.perform(withCorrelationId(get("/accounts/{id}", id1)))
                .andExpect(result -> {
                    var expected = "{\"accountId\":\"" + id1 + "\",\"balance\":300.00,\"currency\":\"BRL\"}";
                    var actual = result.getResponse().getContentAsString();
                    if (!expected.equals(actual)) {
                        throw new AssertionError("Expected: " + expected + " but got: " + actual);
                    }
                })
                .andExpect(status().isOk());

        // get account 2 info
        mvc.perform(withCorrelationId(get("/accounts/{id}", id2)))
                .andExpect(result -> {
                    var expected = "{\"accountId\":\"" + id2 + "\",\"balance\":500.00,\"currency\":\"BRL\"}";
                    var actual = result.getResponse().getContentAsString();
                    if (!expected.equals(actual)) {
                        throw new AssertionError("Expected: " + expected + " but got: " + actual);
                    }
                })
                .andExpect(status().isOk());
    }

    @Test
    void transfer_with_invalid_amount_or_insufficient_balance_or_invalid_destination() throws Exception {
        // open account 1
        var res1 = mvc.perform(withCorrelationId(post("/accounts"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new OpenAccountRequest(new BigDecimal("500.00"), null))))
                .andExpect(status().isOk())
                .andReturn();
        var id1 = UUID.fromString(res1.getResponse().getContentAsString().replace("\"", ""));

        // open account 2
        var res2 = mvc.perform(withCorrelationId(post("/accounts"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new OpenAccountRequest(new BigDecimal("300.00"), null))))
                .andExpect(status().isOk())
                .andReturn();
        var id2 = UUID.fromString(res2.getResponse().getContentAsString().replace("\"", ""));

        // zero transfer
        mvc.perform(withCorrelationId(post("/accounts/{id}/transfer", id1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "accountToTransferId": "%s",
                                  "moneyRequest": {
                                    "amount": 0.00,
                                    "currency": "BRL"
                                  }
                                }
                                """.formatted(id2)))
                .andExpect(status().isBadRequest());

        // negative transfer
        mvc.perform(withCorrelationId(post("/accounts/{id}/transfer", id1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "accountToTransferId": "%s",
                                  "moneyRequest": {
                                    "amount": -0.01,
                                    "currency": "BRL"
                                  }
                                }
                                """.formatted(id2)))
                .andExpect(status().isBadRequest());

        // excessive transfer
        mvc.perform(withCorrelationId(post("/accounts/{id}/transfer", id1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "accountToTransferId": "%s",
                                  "moneyRequest": {
                                    "amount": 500.01,
                                    "currency": "BRL"
                                  }
                                }
                                """.formatted(id2)))
                .andExpect(status().isBadRequest());

        // transfer to self
        mvc.perform(withCorrelationId(post("/accounts/{id}/transfer", id1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "accountToTransferId": "%s",
                                  "moneyRequest": {
                                    "amount": 100.00,
                                    "currency": "BRL"
                                    }
                                    }
                                """.formatted(id1)))
                .andExpect(status().isBadRequest());
    }
}