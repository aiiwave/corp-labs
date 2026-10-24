CREATE TABLE rental_request (
  id            uuid PRIMARY KEY,
  business_key  text NOT NULL UNIQUE,
  status        text NOT NULL,
  title         text NOT NULL,
  created_at    timestamptz NOT NULL DEFAULT now(),
  CONSTRAINT rental_request_status_known
    CHECK (status IN ('REQUESTED', 'APPROVED', 'RENTED'))
);
