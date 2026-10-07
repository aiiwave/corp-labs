package kz.narxoz.carrental.domain;

public class RentalPolicy {

    public RentalStatus move(RentalStatus from, RentalStatus to) {
        if (from == RentalStatus.REQUESTED && to == RentalStatus.APPROVED) {
            return to;
        }
        if (from == RentalStatus.APPROVED && to == RentalStatus.RENTED) {
            return to;
        }
        throw new IllegalStateException("Forbidden transition: " + from + " -> " + to);
    }
}
