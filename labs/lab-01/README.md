# Product

We build a car rental system. People track rental requests.

# Core item

The core item is a **Rental Request**. Each rental request has an ID and a status. The status shows what stage the request is in: it can be requested, approved, or rented.

# Status table

| # | From       | To         | Allowed? |
|---|------------|------------|----------|
| 1 | REQUESTED  | APPROVED   | Yes      |
| 2 | APPROVED   | RENTED     | Yes      |
| 3 | REQUESTED  | RENTED     | No       |
| 4 | RENTED     | REQUESTED  | No       |

# Forbidden — why

- **REQUESTED -> RENTED**: This is forbidden because approval cannot be skipped. Every rental request must be approved first.
- **RENTED -> REQUESTED**: This is forbidden because a completed rental cannot return to the request stage. The process only moves forward.
