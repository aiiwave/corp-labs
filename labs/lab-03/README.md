# Car Rental — Lab 3: tests, SQL, JDBC

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
`src/main/resources/db/schema.sql` is written by hand; nothing generates it.

Start Postgres: `docker compose up -d` (compose runs only the database), or use a local install
with database `css`, user `css`, password `css`.

## Layers

```
domain       RentalId, RentalStatus, RentalPolicy, Rule, DuplicateRental, NotificationPort
             no java.sql, no org.springframework
persistence  RentalJdbc: INSERT, SELECT count(*), status text <-> enum
config       Application, RuleConfig, RentalService, RentalRegistrationService (@Transactional)
client       LogNotificationClient (adapter for the outbound port)
```

## Transactions

- `insertTwice`: one transaction, second INSERT fails, rollback, count is 0.
- `register`: one transaction per call, the first commits, the second throws `DuplicateRental`, count is 1.

Tests do not use `@Transactional`, so the count is read from committed data.

## Run

```
mvn -q verify
```
