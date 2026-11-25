package org.ikigai.timedeposit.deposit.application.service;

import org.ikigai.timedeposit.deposit.application.port.in.ListTimeDepositsUseCase;
import org.ikigai.timedeposit.deposit.domain.TimeDepositRepository;
import org.ikigai.timedeposit.deposit.domain.TimeDepositWithWithdrawals;

import java.util.List;

public class ListTimeDepositsService implements ListTimeDepositsUseCase {

    private final TimeDepositRepository repository;

    public ListTimeDepositsService(TimeDepositRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TimeDepositWithWithdrawals> listTimeDeposits() {
        return repository.findAllWithWithdrawals();
    }
}
