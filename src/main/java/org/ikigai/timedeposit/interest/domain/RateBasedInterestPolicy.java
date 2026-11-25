package org.ikigai.timedeposit.interest.domain;

import org.ikigai.timedeposit.deposit.domain.TimeDeposit;

import java.util.function.IntPredicate;

/** The common shape of today's plans: a yearly rate, earned only on the days the plan's rule allows. */
public record RateBasedInterestPolicy(PlanType planType, AnnualRate rate, IntPredicate earnsInterestOnDay)
        implements InterestPolicy {

    @Override
    public double monthlyInterest(TimeDeposit deposit) {
        return earnsInterestOnDay.test(deposit.getDays()) ? rate.monthlyInterestOn(deposit.getBalance()) : 0;
    }
}
