package org.ikigai.timedeposit.deposit.adapter.in.web;

import org.ikigai.timedeposit.deposit.domain.TimeDepositWithWithdrawals;

import java.math.BigDecimal;
import java.util.List;

record TimeDepositResponse(int id, String planType, BigDecimal balance, int days,
                           List<WithdrawalResponse> withdrawals) {

    static TimeDepositResponse from(TimeDepositWithWithdrawals source) {
        var deposit = source.deposit();
        return new TimeDepositResponse(deposit.getId(), deposit.getPlanType(), source.balance().amount(),
                deposit.getDays(), source.withdrawals().stream().map(WithdrawalResponse::from).toList());
    }
}
