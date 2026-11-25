package org.ikigai.timedeposit.deposit.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "withdrawals")
class WithdrawalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "timeDepositId", nullable = false)
    private Integer timeDepositId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    protected WithdrawalEntity() { }

    Integer getId() { return id; }
    Integer getTimeDepositId() { return timeDepositId; }
    BigDecimal getAmount() { return amount; }
    LocalDate getDate() { return date; }
}
