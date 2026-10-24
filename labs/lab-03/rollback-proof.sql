BEGIN;
INSERT INTO rental_request (id, business_key, status, title)
VALUES ('11111111-1111-1111-1111-111111111111', 'RR-101', 'REQUESTED', 'Toyota Camry, 3 days');
INSERT INTO rental_request (id, business_key, status, title)
VALUES ('22222222-2222-2222-2222-222222222222', 'RR-101', 'REQUESTED', 'Toyota Camry again');
ROLLBACK;
SELECT count(*) FROM rental_request WHERE business_key = 'RR-101';
