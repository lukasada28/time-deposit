package org.ikigai.timedeposit.deposit.adapter.out.persistence;

import org.ikigai.timedeposit.deposit.domain.TimeDeposit;
import org.ikigai.timedeposit.deposit.domain.TimeDepositRepository;
import org.ikigai.timedeposit.deposit.domain.TimeDepositWithWithdrawals;
import org.ikigai.timedeposit.shared.Money;
import org.ikigai.timedeposit.withdrawal.domain.Withdrawal;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Outbound adapter: implements the domain repository with Spring Data JPA. Transactions are set up by the caller. */
@Component
class TimeDepositPersistenceAdapter implements TimeDepositRepository {

    private static final Sort BY_ID = Sort.by("id");

    private final TimeDepositJpaRepository timeDeposits;
    private final WithdrawalJpaRepository withdrawals;

    TimeDepositPersistenceAdapter(TimeDepositJpaRepository timeDeposits, WithdrawalJpaRepository withdrawals) {
        this.timeDeposits = timeDeposits;
        this.withdrawals = withdrawals;
    }

    @Override
    public List<TimeDeposit> findAll() {
        return timeDeposits.findAll(BY_ID).stream().map(TimeDepositPersistenceAdapter::toDomain).toList();
    }

    @Override
    public List<TimeDepositWithWithdrawals> findAllWithWithdrawals() {
        var entities = timeDeposits.findAll(BY_ID);
        // One query for all withdrawals (no N+1).
        var withdrawalsByDeposit = withdrawals
                .findByTimeDepositIdInOrderByDateAscIdAsc(entities.stream().map(TimeDepositEntity::getId).toList())
                .stream()
                .map(TimeDepositPersistenceAdapter::toDomain)
                .collect(Collectors.groupingBy(Withdrawal::timeDepositId));
        return entities.stream()
                .map(entity -> new TimeDepositWithWithdrawals(toDomain(entity),
                        withdrawalsByDeposit.getOrDefault(entity.getId(), List.of())))
                .toList();
    }

    @Override
    public void saveBalances(List<TimeDeposit> deposits) {
        Map<Integer, TimeDeposit> byId = deposits.stream()
                .collect(Collectors.toMap(TimeDeposit::getId, Function.identity()));
        var entities = timeDeposits.findAllById(byId.keySet());
        entities.forEach(entity -> entity.setBalance(Money.of(byId.get(entity.getId()).getBalance()).amount()));
        timeDeposits.saveAll(entities);
    }

    private static TimeDeposit toDomain(TimeDepositEntity entity) {
        return new TimeDeposit(entity.getId(), entity.getPlanType(), entity.getBalance().doubleValue(), entity.getDays());
    }

    private static Withdrawal toDomain(WithdrawalEntity entity) {
        return new Withdrawal(entity.getId(), entity.getTimeDepositId(), new Money(entity.getAmount()), entity.getDate());
    }
}
