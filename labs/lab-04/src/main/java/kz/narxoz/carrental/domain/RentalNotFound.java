package kz.narxoz.carrental.domain;

/** Unchecked: the transaction rolls back and the caller sees which key is missing. */
public class RentalNotFound extends RuntimeException {

    public RentalNotFound(String businessKey) {
        super("rental request not found: " + businessKey);
    }
}
