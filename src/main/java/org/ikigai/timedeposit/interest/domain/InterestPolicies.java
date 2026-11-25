package org.ikigai.timedeposit.interest.domain;

import java.util.List;

/** The bank's current plans. The 30-day grace period is common to all and lives in {@link TimeDepositCalculator}. */
public final class InterestPolicies {
    /** Assumption: "1 year" is 365 days, so a student deposit earns until day 365 and nothing from day 366. */
    private static final int STUDENT_LAST_INTEREST_DAY = 365;
    private static final int PREMIUM_LAST_INTEREST_FREE_DAY = 45;

    private InterestPolicies() { }

    public static InterestPolicy basic() {
        return new RateBasedInterestPolicy(PlanType.BASIC, new AnnualRate(0.01), day -> true);
    }

    public static InterestPolicy student() {
        return new RateBasedInterestPolicy(PlanType.STUDENT, new AnnualRate(0.03), day -> day <= STUDENT_LAST_INTEREST_DAY);
    }

    public static InterestPolicy premium() {
        return new RateBasedInterestPolicy(PlanType.PREMIUM, new AnnualRate(0.05), day -> day > PREMIUM_LAST_INTEREST_FREE_DAY);
    }

    public static List<InterestPolicy> standard() {
        return List.of(basic(), student(), premium());
    }
}
