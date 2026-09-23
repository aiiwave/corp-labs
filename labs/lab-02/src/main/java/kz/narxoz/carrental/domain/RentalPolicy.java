package kz.narxoz.carrental.domain;

/**
 * Business rules for changing the status of a rental request.
 * Kept from Lab 1 — TransitionRule delegates to this class.
 */
public class RentalPolicy {

    public RentalStatus move(RentalStatus from, RentalStatus to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        if (isAllowed(from, to)) {
            return to;
        }
        throw new IllegalStateException(
                "Transition from " + from + " to " + to + " is not allowed");
    }

    private boolean isAllowed(RentalStatus from, RentalStatus to) {
        return (from == RentalStatus.REQUESTED && to == RentalStatus.APPROVED)
                || (from == RentalStatus.APPROVED && to == RentalStatus.RENTED);
    }
}
