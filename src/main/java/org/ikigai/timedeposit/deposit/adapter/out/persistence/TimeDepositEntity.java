package org.ikigai.timedeposit.deposit.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "timeDeposits")
class TimeDepositEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "planType", nullable = false)
    private String planType;

    @Column(name = "days", nullable = false)
    private int days;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    protected TimeDepositEntity() { }

    Integer getId() { return id; }
    String getPlanType() { return planType; }
    int getDays() { return days; }
    BigDecimal getBalance() { return balance; }
    void setBalance(BigDecimal balance) { this.balance = balance; }
}
