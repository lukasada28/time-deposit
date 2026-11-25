package org.ikigai.timedeposit.interest.domain;

/** Value object: a yearly interest rate (0.03 = 3%), paid in twelve equal monthly instalments. */
public record AnnualRate(double value) {
    private static final int MONTHS_PER_YEAR = 12;

    public double monthlyInterestOn(double balance) {
        return balance * value / MONTHS_PER_YEAR;
    }
}
