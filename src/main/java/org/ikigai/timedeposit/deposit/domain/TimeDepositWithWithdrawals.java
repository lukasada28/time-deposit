package org.ikigai.timedeposit.deposit.domain;

import org.ikigai.timedeposit.shared.Money;
import org.ikigai.timedeposit.withdrawal.domain.Withdrawal;

import java.util.List;

/**
 * A time deposit together with its withdrawals. Withdrawals live beside the shared {@link TimeDeposit}
 * (not inside it) so that class stays untouched, as required.
 */
public record TimeDepositWithWithdrawals(TimeDeposit deposit, List<Withdrawal> withdrawals) {

    public TimeDepositWithWithdrawals {
        withdrawals = List.copyOf(withdrawals);
    }

    public Money balance() {
        return Money.of(deposit.getBalance());
    }
}
