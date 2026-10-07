package kz.narxoz.carrental.domain;

/** Unchecked on purpose: Spring rolls back the transaction only for unchecked exceptions. */
public class DuplicateRental extends RuntimeException {

    public DuplicateRental(String businessKey) {
        super("duplicate rental request: " + businessKey);
    }
}
