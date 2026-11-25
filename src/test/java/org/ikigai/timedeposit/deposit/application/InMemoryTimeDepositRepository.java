package org.ikigai.timedeposit.deposit.application;

import org.ikigai.timedeposit.deposit.domain.TimeDeposit;
import org.ikigai.timedeposit.deposit.domain.TimeDepositRepository;
import org.ikigai.timedeposit.deposit.domain.TimeDepositWithWithdrawals;

import java.util.ArrayList;
import java.util.List;

/** Hand-written fake instead of a mock: the tests assert outcomes, not calls. */
class InMemoryTimeDepositRepository implements TimeDepositRepository {

    private final List<TimeDeposit> store = new ArrayList<>();

    void add(TimeDeposit deposit) { store.add(deposit); }

    @Override public List<TimeDeposit> findAll() { return List.copyOf(store); }

    @Override public List<TimeDepositWithWithdrawals> findAllWithWithdrawals() {
        return store.stream().map(deposit -> new TimeDepositWithWithdrawals(deposit, List.of())).toList();
    }

    @Override public void saveBalances(List<TimeDeposit> deposits) { /* same instances are shared in memory */ }
}
