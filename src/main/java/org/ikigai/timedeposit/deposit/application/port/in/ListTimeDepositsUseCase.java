package org.ikigai.timedeposit.deposit.application.port.in;

import org.ikigai.timedeposit.deposit.domain.TimeDepositWithWithdrawals;

import java.util.List;

@FunctionalInterface
public interface ListTimeDepositsUseCase {
    List<TimeDepositWithWithdrawals> listTimeDeposits();
}
