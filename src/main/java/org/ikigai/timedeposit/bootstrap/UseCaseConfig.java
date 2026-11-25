package org.ikigai.timedeposit.bootstrap;

import org.ikigai.timedeposit.deposit.application.port.in.ApplyMonthlyInterestUseCase;
import org.ikigai.timedeposit.deposit.application.port.in.ListTimeDepositsUseCase;
import org.ikigai.timedeposit.deposit.application.service.ApplyMonthlyInterestService;
import org.ikigai.timedeposit.deposit.application.service.ListTimeDepositsService;
import org.ikigai.timedeposit.deposit.domain.TimeDepositRepository;
import org.ikigai.timedeposit.interest.domain.TimeDepositCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Composition root. Domain and application classes are plain Java; this is the only place that wires them to
 * Spring and wraps each use case in a transaction (so applying interest is all-or-nothing).
 */
@Configuration
class UseCaseConfig {

    @Bean
    TimeDepositCalculator timeDepositCalculator() {
        return new TimeDepositCalculator();
    }

    @Bean
    ApplyMonthlyInterestUseCase applyMonthlyInterestUseCase(TimeDepositRepository repository,
                                                            TimeDepositCalculator calculator,
                                                            PlatformTransactionManager transactionManager) {
        var useCase = new ApplyMonthlyInterestService(repository, calculator);
        var transaction = new TransactionTemplate(transactionManager);
        return () -> transaction.executeWithoutResult(_ -> useCase.applyMonthlyInterest());
    }

    @Bean
    ListTimeDepositsUseCase listTimeDepositsUseCase(TimeDepositRepository repository,
                                                    PlatformTransactionManager transactionManager) {
        var useCase = new ListTimeDepositsService(repository);
        var transaction = new TransactionTemplate(transactionManager);
        transaction.setReadOnly(true);
        return () -> transaction.execute(_ -> useCase.listTimeDeposits());
    }
}
