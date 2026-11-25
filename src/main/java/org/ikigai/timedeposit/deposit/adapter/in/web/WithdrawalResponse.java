package org.ikigai.timedeposit.deposit.adapter.in.web;

import org.ikigai.timedeposit.withdrawal.domain.Withdrawal;

import java.math.BigDecimal;
import java.time.LocalDate;

record WithdrawalResponse(int id, BigDecimal amount, LocalDate date) {

    static WithdrawalResponse from(Withdrawal withdrawal) {
        return new WithdrawalResponse(withdrawal.id(), withdrawal.amount().amount(), withdrawal.date());
    }
}
