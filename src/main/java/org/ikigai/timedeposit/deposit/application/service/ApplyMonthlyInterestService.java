package org.ikigai.timedeposit.deposit.application.service;

import org.ikigai.timedeposit.deposit.application.port.in.ApplyMonthlyInterestUseCase;
import org.ikigai.timedeposit.deposit.domain.TimeDepositRepository;
import org.ikigai.timedeposit.interest.domain.TimeDepositCalculator;

/** Orchestration only: load, let the domain calculate, store. Transactions are added around it at wiring time. */
public class ApplyMonthlyInterestService implements ApplyMonthlyInterestUseCase {

    private final TimeDepositRepository repository;
    private final TimeDepositCalculator calculator;

    public ApplyMonthlyInterestService(TimeDepositRepository repository, TimeDepositCalculator calculator) {
        this.repository = repository;
        this.calculator = calculator;
    }

    @Override
    public void applyMonthlyInterest() {
        var deposits = repository.findAll();
        calculator.updateBalance(deposits);
        repository.saveBalances(deposits);
    }
}
