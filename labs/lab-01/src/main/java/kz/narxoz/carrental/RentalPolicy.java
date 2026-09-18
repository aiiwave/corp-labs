package kz.narxoz.carrental;

/**
 * Business rules for changing the status of a rental request.
 */
public class RentalPolicy {

    /**
     * Moves a rental request from one status to another.
     *
     * @param from current status
     * @param to   target status
     * @return the new status, if the transition is allowed
     * @throws IllegalArgumentException if from or to is null
     * @throws IllegalStateException    if the transition is forbidden
     */
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
