package org.ikigai.timedeposit.interest.domain;

import java.util.Arrays;
import java.util.Optional;

/**
 * The plans the bank offers (ubiquitous language). The shared TimeDeposit keeps the plan as a String, so this enum
 * is the typed view of it. Matching is exact, like the legacy code; an unknown code is simply empty.
 */
public enum PlanType {
    BASIC("basic"),
    STUDENT("student"),
    PREMIUM("premium");

    private final String code;

    PlanType(String code) {
        this.code = code;
    }

    public static Optional<PlanType> fromCode(String code) {
        return Arrays.stream(values()).filter(type -> type.code.equals(code)).findFirst();
    }
}
