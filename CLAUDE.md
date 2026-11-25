# Project rules for the AI coding assistant (Claude Code)

- Java 25, Spring Boot 3.5, Maven, Liquibase, PostgreSQL. Package by subdomain, hexagonal layers inside each.
- Subdomains: `interest` (core pricing rules), `deposit` (the deposit use cases, API, persistence), `withdrawal`, `shared`.
- NEVER change the shared `TimeDeposit` class or the signature/behaviour of `TimeDepositCalculator.updateBalance`.
- `*.domain` and `*.application` are plain Java: no Spring, JPA or Jackson imports. Repository interfaces live in the domain.
- Dependencies point inwards only: adapter -> application -> domain. No cycles between subdomains
  (interest -> deposit.domain, deposit.application -> interest.domain, deposit.domain -> withdrawal.domain + shared).
- Business rules live in the domain, never in controllers or adapters.
- Schema changes only through new Liquibase changesets (never edit an applied one); lowercase table/column names.
- Exactly two endpoints (see `static/openapi.yaml`). Do not add more.
- Tests first for behaviour changes; assert outcomes, no mock-verification tests. Boundaries: 29/30/31, 45/46, 365/366.
- Small steps, `mvn test` after each, one logical change per commit, conventional commit messages.
- No dead code, no speculative abstractions. State assumptions in code comments.
