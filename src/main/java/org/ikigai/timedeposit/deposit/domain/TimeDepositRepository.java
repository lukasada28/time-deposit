package org.ikigai.timedeposit.deposit.domain;

import java.util.List;

/**
 * Repository: a domain concept (DDD) whose implementation lives in an outbound adapter (hexagonal).
 * Nothing here knows about JPA or SQL.
 */
public interface TimeDepositRepository {

    List<TimeDeposit> findAll();

    List<TimeDepositWithWithdrawals> findAllWithWithdrawals();

    void saveBalances(List<TimeDeposit> deposits);
}
