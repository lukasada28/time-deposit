package org.ikigai.timedeposit.interest.domain;

import org.ikigai.timedeposit.deposit.domain.TimeDeposit;

/**
 * Strategy: the interest rule of one plan, applied once the common grace period is over.
 * New rule = new implementation; {@link TimeDepositCalculator} does not change (open/closed).
 */
public interface InterestPolicy {

    PlanType planType();

    /** Interest earned in one month. Only called for deposits older than the grace period. */
    double monthlyInterest(TimeDeposit deposit);
}
