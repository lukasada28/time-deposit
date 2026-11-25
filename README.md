# XA Bank Time Deposit (Java 25, Spring Boot, PostgreSQL, Liquibase)

## Run
Requirements: JDK 25, Maven, Docker.

    docker compose up -d        # PostgreSQL
    mvn spring-boot:run         # Liquibase creates the tables and loads demo data (context "demo")
    mvn test                    # unit tests + Testcontainers end-to-end test (Docker required)

## Trigger the endpoints with Swagger
1. Open http://localhost:8080/swagger-ui.html (contract: `src/main/resources/static/openapi.yaml`).
2. `GET /time-deposits` -> Try it out -> Execute: all deposits with `id, planType, balance, days, withdrawals`.
3. `PUT /time-deposits/balances` -> Try it out -> Execute: applies one month of interest (204); GET again to see it.

## Structure: package by subdomain, hexagonal inside
    org.ikigai.timedeposit
    ├── interest/            core subdomain: the interest rules
    │   └── domain/            TimeDepositCalculator, InterestPolicy (+ per-plan policies), PlanType, AnnualRate
    ├── deposit/             the time deposit and what we do with it
    │   ├── domain/            TimeDeposit (unchanged), TimeDepositWithWithdrawals, TimeDepositRepository
    │   ├── application/       use-case ports (ApplyMonthlyInterest, ListTimeDeposits) + services
    │   └── adapter/           in/web (REST), out/persistence (JPA)
    ├── withdrawal/domain/   Withdrawal
    ├── shared/              Money (shared kernel)
    ├── bootstrap/           composition root: wiring + transactions
    └── TimeDepositApplication

Dependencies only point inwards and never form cycles:
`deposit.adapter -> deposit.application -> interest.domain -> deposit.domain -> withdrawal.domain, shared`.
Domain and application code is plain Java (no Spring/JPA). A new plan rule = a new `InterestPolicy` (open/closed).

## Database (Liquibase)
Changelogs in `src/main/resources/db/changelog`: `001` time deposits, `002` withdrawals, `003` demo data (context `demo`).
Table/column names are lowercase in the changelog on purpose; they are the brief's `timeDeposits`, `planType`, `timeDepositId`
as PostgreSQL stores them (unquoted names are folded to lowercase), so Hibernate's unquoted queries match.

## Assumptions
- `updateBalance` behaviour is preserved exactly: interest only when days > 30; student none from day 366; premium only when days > 45; unknown plan = no interest.
- "One year" = 365 days.
- The shared `TimeDeposit` keeps a `Double` balance (must not change); `Money` converts to 2 decimals HALF_UP for storage and API.
- Withdrawals are a separate read model, not part of `TimeDeposit`.
- `PUT /time-deposits/balances`: no body, 204. Not idempotent: every call adds one month of interest.
- No validation or error handling, per the brief.

## AI usage
See `CLAUDE.md` and `docs/AI_WORKFLOW.md`.
