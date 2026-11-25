package org.ikigai.timedeposit.deposit.application.port.in;

/** Credits one month of interest to every time deposit. */
@FunctionalInterface
public interface ApplyMonthlyInterestUseCase {
    void applyMonthlyInterest();
}
