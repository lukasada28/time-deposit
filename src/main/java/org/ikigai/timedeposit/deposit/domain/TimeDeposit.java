package org.ikigai.timedeposit.deposit.domain;

/**
 * NOTE: this is the shared class from the kata. KEEP YOUR ORIGINAL (and its original package) - do not replace it
 * with this copy. It is only here so this drop-in compiles on its own. It must not change.
 */
public class TimeDeposit {
    private final int id;
    private final String planType;
    private Double balance;
    private final int days;

    public TimeDeposit(int id, String planType, Double balance, int days) {
        this.id = id;
        this.planType = planType;
        this.balance = balance;
        this.days = days;
    }

    public int getId() { return id; }
    public String getPlanType() { return planType; }
    public Double getBalance() { return balance; }
    public int getDays() { return days; }
    public void setBalance(Double balance) { this.balance = balance; }
}
