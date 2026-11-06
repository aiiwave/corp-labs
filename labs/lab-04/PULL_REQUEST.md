# lab-04: REST contract, error record, springdoc, yaml profiles, actuator

- [x] Story IDs in the title
- [x] Same product as Labs 1–3 (Car Rental / Rental Request)
- [x] POST and GET under /api/v1/rental-requests. Path id is a UUID.
- [x] 201 + Location, 400 VALIDATION (field name), 404 NOT_FOUND, 409 DUPLICATE
- [x] Second POST of the same business key is 409 and the row count stays 1 (RentalApiIntegrationTest.secondPostOfSameKeyIs409AndCountStaysOne)
- [x] Error JSON is only code, message, correlationId. Stack traces stay off the wire
- [x] Response type is not the entity. Domain has no Spring and no jakarta.validation
- [x] /swagger-ui.html shows 201, 400, 409 on create and 200, 404 on get
- [x] @WebMvcTest covers 201, 400, 404, 409, and the correlation header
- [x] Datasource and app.downstream (base URL + timeout) come from yaml
- [x] dev profile is localhost css/css. prod uses env vars. No cloud password in git
- [x] GET /actuator/health is 200. env and heapdump are 404
- [x] Lab 3 tests still green. mvn -q verify is green
- [x] No Spring Security, no Dockerfile, no target/ in git
