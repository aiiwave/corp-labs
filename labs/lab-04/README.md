# Car Rental — Lab 4: REST contract and Spring Boot platform

Same product as Labs 1–3: Car Rental, core item Rental Request, business key `RR-101`.

## API (`/api/v1/rental-requests`)

| Call | Result |
| --- | --- |
| `POST /api/v1/rental-requests` `{businessKey, title}` | 201 + `Location`, body `{id, businessKey, status, title}` |
| `GET /api/v1/rental-requests/{id}` (id = UUID) | 200, same body |
| blank key/title, title > 120, id not UUID, body not JSON | 400 `VALIDATION` |
| UUID not in table | 404 `NOT_FOUND` |
| business key already stored | 409 `DUPLICATE` (first row stays, count is 1) |
| wrong Content-Type | 415 `UNSUPPORTED` |
| unmapped bug | 500 `INTERNAL`, message `unexpected error` |

Every error body: `{code, message, correlationId}`. `X-Correlation-Id` is echoed, or minted if missing.

Swagger UI: `/swagger-ui.html`, spec: `/v3/api-docs`.

## Layers

```
domain       Rental (record), RentalId, RentalStatus, rules, DuplicateRental, RentalNotFound. No Spring, no jakarta.validation
handler      RentalController, RentalRequest, RentalResponse, ApiError, ApiExceptionHandler
persistence  RentalEntity (never on the wire), RentalJpa, RentalJdbc
config       Application, services, NotificationConfig (@EnableConfigurationProperties)
client       DownstreamProperties (app.downstream), LogNotificationClient
```

## Profiles

- `application.yaml`: shared (ddl-auto validate, open-in-view false, error leak off, actuator health+info, `spring.profiles.default: dev`). No password.
- `application-dev.yaml`: localhost PostgreSQL `css`/`css`/`css`, downstream `http://localhost:9090`, timeout `2s`.
- `application-prod.yaml`: `CSS_DB_URL`, `CSS_DB_USER`, `CSS_DB_PASSWORD` (and optional `CSS_DOWNSTREAM_URL`). Run with `SPRING_PROFILES_ACTIVE=prod`.

## Actuator

`/actuator/health` 200 UP (no details), `/actuator/info` 200. `env`, `beans`, `heapdump`, `shutdown` -> 404.

## Run

```
docker compose up -d
mvn -q verify
mvn spring-boot:run
```
