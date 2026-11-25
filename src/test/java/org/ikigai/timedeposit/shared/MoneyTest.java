package org.ikigai.timedeposit.shared;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MoneyTest {

    @Test
    void keepsTwoDecimalsRoundedHalfUp() {
        assertThat(Money.of(1200.0).amount()).hasToString("1200.00");
        assertThat(Money.of(1.005).amount()).hasToString("1.01");
        assertThat(Money.of(1201.00000000000001).amount()).hasToString("1201.00");
    }
}
