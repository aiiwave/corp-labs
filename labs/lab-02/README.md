# Product

Same as Lab 1: we build a car rental system. People track rental requests.

# What this lab adds

Lab 1 built the business rules in plain Java. Lab 2 joins Spring Boot on
top of the same domain, without changing the business rules themselves.

- `Application` (in `config`) starts the Spring Boot process.
- `RentalService`, a `@Service` in `config`, injects a `Rule`.
- `Rule` and its two implementations (`TransitionRule`, `TerminalStatusRule`)
  live in `domain` and have no Spring import.
- `RuleConfig` wires the two rule implementations into one `Rule` bean
  (`CompositeRule`) with a `@Bean`.

# Package diagram

```
  dto          client        handler         config
  (JSON later) (HTTP later)  (HTTP week 9)   Application
                                             RentalService (@Service)
       \            \            /                |
        \            \          /           injects Rule
         \            \        /
                     domain
              RentalId  RentalStatus  RentalPolicy
              Rule + TransitionRule + TerminalStatusRule
                     (no Spring)
```

Arrows point inward. `domain` never imports `org.springframework`.

# Rule implementations

- **TransitionRule** — the status table from Lab 1: REQUESTED -> APPROVED ->
  RENTED, nothing else. Delegates to `RentalPolicy.move`, so the table is
  defined in exactly one place.
- **TerminalStatusRule** — the stop-factor. RENTED is a terminal status:
  once a rental reaches it, no further transition is allowed at all,
  no matter the target status.

`CompositeRule` runs both rules in order, so either one can stop a move.

# How to run

```
mvn spring-boot:run
```

This starts the Spring context, then prints the result of one demo move
(REQUESTED -> APPROVED) and exits, since there is no web server in this lab.

# How to test

```
mvn -q verify
```
