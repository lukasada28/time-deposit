package org.ikigai.timedeposit.deposit.application;

import org.ikigai.timedeposit.deposit.application.service.ApplyMonthlyInterestService;
import org.ikigai.timedeposit.deposit.application.service.ListTimeDepositsService;
import org.ikigai.timedeposit.deposit.domain.TimeDeposit;
import org.ikigai.timedeposit.interest.domain.TimeDepositCalculator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class UseCasesTest {

    private final InMemoryTimeDepositRepository repository = new InMemoryTimeDepositRepository();

    @Test
    void applyingMonthlyInterestUpdatesTheBalanceOfEveryDeposit() {
        repository.add(new TimeDeposit(1, "basic", 1200.0, 90));
        repository.add(new TimeDeposit(2, "premium", 2400.0, 10));

        new ApplyMonthlyInterestService(repository, new TimeDepositCalculator()).applyMonthlyInterest();

        var balances = repository.findAll().stream().map(TimeDeposit::getBalance).toList();
        assertThat(balances.get(0)).isCloseTo(1201.0, within(1e-9)); // older than 30 days -> interest
        assertThat(balances.get(1)).isEqualTo(2400.0);                // inside the grace period
    }

    @Test
    void listingReturnsEveryDeposit() {
        repository.add(new TimeDeposit(1, "basic", 1200.0, 90));
        repository.add(new TimeDeposit(2, "student", 500.0, 40));

        var result = new ListTimeDepositsService(repository).listTimeDeposits();

        assertThat(result).extracting(item -> item.deposit().getId()).containsExactly(1, 2);
    }
}
