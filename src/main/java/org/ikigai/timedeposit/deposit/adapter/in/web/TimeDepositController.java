package org.ikigai.timedeposit.deposit.adapter.in.web;

import org.ikigai.timedeposit.deposit.application.port.in.ApplyMonthlyInterestUseCase;
import org.ikigai.timedeposit.deposit.application.port.in.ListTimeDepositsUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Implements src/main/resources/static/openapi.yaml - exactly two endpoints. */
@RestController
@RequestMapping("/time-deposits")
class TimeDepositController {

    private final ListTimeDepositsUseCase listTimeDeposits;
    private final ApplyMonthlyInterestUseCase applyMonthlyInterest;

    TimeDepositController(ListTimeDepositsUseCase listTimeDeposits, ApplyMonthlyInterestUseCase applyMonthlyInterest) {
        this.listTimeDeposits = listTimeDeposits;
        this.applyMonthlyInterest = applyMonthlyInterest;
    }

    @GetMapping
    List<TimeDepositResponse> getAllTimeDeposits() {
        return listTimeDeposits.listTimeDeposits().stream().map(TimeDepositResponse::from).toList();
    }

    /** Assumption: PUT on the "balances" collection = "recalculate all balances"; no body, 204 on success. */
    @PutMapping("/balances")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateAllBalances() {
        applyMonthlyInterest.applyMonthlyInterest();
    }
}
