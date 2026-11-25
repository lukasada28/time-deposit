package org.ikigai.timedeposit.interest.domain;

import org.ikigai.timedeposit.deposit.domain.TimeDeposit;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Domain service: applies one month of interest. {@code updateBalance(List<TimeDeposit>)} keeps its signature and
 * its behaviour; the old if/else chain on the plan type became one {@link InterestPolicy} per plan.
 *
 * Assumption: a deposit with an unknown plan type earns nothing (the legacy code silently ignored it too).
 */
public class TimeDepositCalculator {
    /** No interest for any plan during the first 30 days. */
    private static final int GRACE_PERIOD_DAYS = 30;

    private final Map<PlanType, InterestPolicy> policies = new EnumMap<>(PlanType.class);

    public TimeDepositCalculator() {
        this(InterestPolicies.standard());
    }

    public TimeDepositCalculator(List<InterestPolicy> policies) {
        policies.forEach(policy -> {
            if (this.policies.putIfAbsent(policy.planType(), policy) != null) {
                throw new IllegalArgumentException("Two interest policies for " + policy.planType());
            }
        });
    }

    public void updateBalance(List<TimeDeposit> xs) {
        xs.stream().filter(TimeDepositCalculator::isPastGracePeriod).forEach(this::addMonthlyInterest);
    }

    private static boolean isPastGracePeriod(TimeDeposit deposit) {
        return deposit.getDays() > GRACE_PERIOD_DAYS;
    }

    private void addMonthlyInterest(TimeDeposit deposit) {
        PlanType.fromCode(deposit.getPlanType())
                .map(policies::get)
                .ifPresent(policy -> deposit.setBalance(deposit.getBalance() + policy.monthlyInterest(deposit)));
    }
}
