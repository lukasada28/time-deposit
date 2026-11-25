package org.ikigai.timedeposit.deposit.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

interface WithdrawalJpaRepository extends JpaRepository<WithdrawalEntity, Integer> {

    List<WithdrawalEntity> findByTimeDepositIdInOrderByDateAscIdAsc(Collection<Integer> timeDepositIds);
}
