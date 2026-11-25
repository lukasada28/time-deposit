package org.ikigai.timedeposit.interest.domain;

import org.ikigai.timedeposit.deposit.domain.TimeDeposit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.within;

class TimeDepositCalculatorTest {

    private static final double START = 1000.0;
    private final TimeDepositCalculator calculator = new TimeDepositCalculator();

    @ParameterizedTest(name = "{0} plan at {1} days earns {2}% yearly interest")
    @CsvSource({
            // nobody earns anything during the first 30 days; day 31 is the first one that counts
            "basic,   29, 0", "basic,   30, 0", "basic,   31, 1", "basic, 1000, 1",
            // student: 3%, none from day 366 on
            "student, 29, 0", "student, 30, 0", "student, 31, 3", "student, 365, 3", "student, 366, 0",
            // premium: 5%, only after day 45
            "premium, 31, 0", "premium, 45, 0", "premium, 46, 5",
            // unknown plans are ignored, as in the legacy code
            "gold,    100, 0"
    })
    void appliesOneMonthOfInterestAccordingToPlanAndAge(String plan, int days, double yearlyPercent) {
        var deposit = new TimeDeposit(1, plan, START, days);

        calculator.updateBalance(List.of(deposit));

        assertThat(deposit.getBalance()).isCloseTo(START + START * yearlyPercent / 100 / 12, within(1e-9));
    }

    @Test
    void updatesEveryDepositInTheList() {
        var basic = new TimeDeposit(1, "basic", 1200.0, 60);
        var student = new TimeDeposit(2, "student", 1200.0, 60);

        calculator.updateBalance(List.of(basic, student));

        assertThat(basic.getBalance()).isCloseTo(1201.0, within(1e-9));
        assertThat(student.getBalance()).isCloseTo(1203.0, within(1e-9));
    }

    @Test
    void aPlanRuleCanBeReplacedWithoutTouchingTheCalculator() {
        InterestPolicy flatTenForBasic = new InterestPolicy() {
            @Override public PlanType planType() { return PlanType.BASIC; }
            @Override public double monthlyInterest(TimeDeposit deposit) { return 10; }
        };
        var deposit = new TimeDeposit(1, "basic", 100.0, 31);

        new TimeDepositCalculator(List.of(flatTenForBasic)).updateBalance(List.of(deposit));

        assertThat(deposit.getBalance()).isEqualTo(110.0);
    }

    @Test
    void rejectsTwoPoliciesForTheSamePlan() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> new TimeDepositCalculator(List.of(InterestPolicies.basic(), InterestPolicies.basic())));
    }
}
