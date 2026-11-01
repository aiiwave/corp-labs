package kz.narxoz.carrental.domain;

/** Rule 1: the status table. */
public class TransitionRule implements Rule {

    private final RentalPolicy policy = new RentalPolicy();

    @Override
    public void check(RentalStatus from, RentalStatus to) {
        policy.move(from, to);
    }
}
