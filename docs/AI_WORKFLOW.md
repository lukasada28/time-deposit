# AI-assisted development

**Tools:** Claude Code (terminal agent) with the rules in `CLAUDE.md` loaded automatically as project memory.
(Edit this section to match what you actually used.)

**Setup / reproduce:** `npm i -g @anthropic-ai/claude-code`, run `claude` in the repo root, `/login`.

**Workflow:** requirements -> read existing code -> list gaps -> characterization tests for `updateBalance`
boundaries -> refactor the calculator to policies -> split into subdomains -> use cases and ports -> adapters,
Liquibase, OpenAPI -> Testcontainers test. The agent proposed each step, I reviewed the diff, ran `mvn test`, then committed.

**AI-assisted:** boilerplate (entities, DTOs, changelogs, pom), test tables, README drafts.
**Human-decided/reviewed:** subdomain split, domain model, assumptions in the README, every line of the final diff.
