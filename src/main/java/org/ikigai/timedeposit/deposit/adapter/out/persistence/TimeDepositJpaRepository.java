package org.ikigai.timedeposit.deposit.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

interface TimeDepositJpaRepository extends JpaRepository<TimeDepositEntity, Integer> { }
