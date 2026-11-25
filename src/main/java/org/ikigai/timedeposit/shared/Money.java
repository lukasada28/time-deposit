package org.ikigai.timedeposit.shared;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Shared kernel: an amount we show and store, always 2 decimals, HALF_UP (assumption: cents are the smallest unit).
 * The shared TimeDeposit keeps its balance as a Double; this is where that gets converted, in one place.
 */
public record Money(BigDecimal amount) {
    private static final int SCALE = 2;

    public Money {
        amount = amount.setScale(SCALE, RoundingMode.HALF_UP);
    }

    public static Money of(double value) {
        return new Money(BigDecimal.valueOf(value));
    }
}
