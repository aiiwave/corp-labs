package kz.narxoz.carrental.domain;

/** Rule 2 (stop-factor): a RENTED request can not move anywhere. */
public class TerminalStatusRule implements Rule {

    @Override
    public void check(RentalStatus from, RentalStatus to) {
        if (from == RentalStatus.RENTED) {
            throw new IllegalStateException("RENTED is final, can not move to " + to);
        }
    }
}
