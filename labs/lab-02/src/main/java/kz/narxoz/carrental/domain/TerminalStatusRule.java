package kz.narxoz.carrental.domain;

/**
 * Rule #2: the stop-factor.
 * RENTED is a terminal status — once a rental reaches it,
 * no further transition is allowed at all, no matter the target.
 */
public class TerminalStatusRule implements Rule {

    @Override
    public void check(RentalStatus from, RentalStatus to) {
        if (from == RentalStatus.RENTED) {
            throw new IllegalStateException(
                    "Rental is already RENTED, which is a terminal status; "
                            + "no further changes are allowed");
        }
    }
}
