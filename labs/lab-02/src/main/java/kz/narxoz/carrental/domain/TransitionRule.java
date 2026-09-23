package kz.narxoz.carrental.domain;

/**
 * Rule #1: the status table from Lab 1.
 * REQUESTED -> APPROVED -> RENTED, nothing else.
 * Reuses RentalPolicy instead of duplicating the table.
 */
public class TransitionRule implements Rule {

    private final RentalPolicy policy = new RentalPolicy();

    @Override
    public void check(RentalStatus from, RentalStatus to) {
        policy.move(from, to);
    }
}
