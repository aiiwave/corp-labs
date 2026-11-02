# Car Rental — Lab 3: tests, SQL, JDBC, JPA, Flyway

Same product as Labs 1 and 2: Car Rental. Core item: Rental Request.

## Status table

| From | To | Result |
| --- | --- | --- |
| REQUESTED | APPROVED | allowed |
| APPROVED | RENTED | allowed |
| REQUESTED | RENTED | forbidden |
| RENTED | REQUESTED | forbidden |

## Forbidden — why

A request must be approved before the car is handed over. RENTED is final.

## Database

PostgreSQL only (no H2). One table `rental_request`: `id` (uuid, surrogate key),
`business_key` (`RR-101`, UNIQUE NOT NULL), `status` (text + CHECK), `title`, `created_at`.

The schema is `src/main/resources/db/migration/V1__create_rental_request.sql`, written by hand.
Flyway runs it on startup. `ddl-auto` is `validate`: Hibernate only checks that the entity
matches the table. Any schema change is a new file (V2, V3), V1 is never edited.

Start Postgres: `docker compose up -d` (compose runs only the database), or use a local install
with database `css`, user `css`, password `css`.

## Layers

```
domain       RentalId, RentalStatus, RentalPolicy, Rule, DuplicateRental, RentalNotFound, NotificationPort
             no java.sql, no jakarta.persistence, no org.springframework
persistence  RentalEntity (maps the table), RentalJpa: persist + flush, count, status text <-> enum
             RentalJdbc: plain JDBC (JdbcTemplate), hand-written SQL with ? parameters
config       Application, RuleConfig, RentalService, RentalRegistrationService, RentalStatusService (@Transactional)
client       LogNotificationClient (adapter for the outbound port)
```

## Transactions

- `insertTwice`: one transaction, second INSERT fails, rollback, count is 0.
- `register`: one transaction per call, the first commits, the second throws `DuplicateRental`, count is 1.
- `RentalStatusService.move`: read status (JDBC), check rules, UPDATE, notify, in one transaction. A forbidden move throws before the UPDATE, so the row is unchanged.

Tests do not use `@Transactional`, so the count is read from committed data.
`@BeforeEach` clears the table with `DELETE FROM`, it never drops it.

## Run

```
docker compose down -v
docker compose up -d
mvn -q verify
```
