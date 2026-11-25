package org.ikigai.timedeposit.deposit.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** End-to-end: HTTP -> use cases -> domain -> JPA -> real PostgreSQL schema from Liquibase (Testcontainers). Needs Docker. */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class TimeDepositApiTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @Autowired MockMvc mvc;
    @Autowired JdbcTemplate jdbc;

    @BeforeEach
    void givenKnownData() {
        jdbc.update("DELETE FROM withdrawals");
        jdbc.update("DELETE FROM timeDeposits");
        jdbc.update("INSERT INTO timeDeposits (id, planType, days, balance) VALUES (1, 'basic', 31, 1200.00)");
        jdbc.update("INSERT INTO timeDeposits (id, planType, days, balance) VALUES (2, 'student', 100, 1200.00)");
        jdbc.update("INSERT INTO timeDeposits (id, planType, days, balance) VALUES (3, 'premium', 50, 2400.00)");
        jdbc.update("INSERT INTO timeDeposits (id, planType, days, balance) VALUES (4, 'premium', 10, 2400.00)");
        jdbc.update("INSERT INTO withdrawals (timeDepositId, amount, date) VALUES (1, 100.00, '2026-01-15')");
    }

    @Test
    void getReturnsAllDepositsWithTheirWithdrawals() throws Exception {
        mvc.perform(get("/time-deposits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].planType").value("basic"))
                .andExpect(jsonPath("$[0].days").value(31))
                .andExpect(jsonPath("$[0].balance").value(1200.0))
                .andExpect(jsonPath("$[0].withdrawals.length()").value(1))
                .andExpect(jsonPath("$[0].withdrawals[0].amount").value(100.0))
                .andExpect(jsonPath("$[0].withdrawals[0].date").value("2026-01-15"))
                .andExpect(jsonPath("$[1].withdrawals.length()").value(0));
    }

    @Test
    void putAppliesMonthlyInterestToEveryDepositAndPersistsIt() throws Exception {
        mvc.perform(put("/time-deposits/balances")).andExpect(status().isNoContent());

        mvc.perform(get("/time-deposits"))
                .andExpect(jsonPath("$[0].balance").value(1201.0))   // basic: 1200 * 1% / 12
                .andExpect(jsonPath("$[1].balance").value(1203.0))   // student: 1200 * 3% / 12
                .andExpect(jsonPath("$[2].balance").value(2410.0))   // premium past day 45: 2400 * 5% / 12
                .andExpect(jsonPath("$[3].balance").value(2400.0));  // premium inside grace period
    }
}
