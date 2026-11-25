package org.ikigai.timedeposit.withdrawal.domain;

import org.ikigai.timedeposit.shared.Money;

import java.time.LocalDate;

/** Money taken out of a time deposit on a given day. */
public record Withdrawal(int id, int timeDepositId, Money amount, LocalDate date) { }
