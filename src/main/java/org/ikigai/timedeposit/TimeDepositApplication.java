package org.ikigai.timedeposit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Lives in the root package so component, entity and repository scanning cover every subdomain. */
@SpringBootApplication
public class TimeDepositApplication {
    public static void main(String[] args) {
        SpringApplication.run(TimeDepositApplication.class, args);
    }
}
