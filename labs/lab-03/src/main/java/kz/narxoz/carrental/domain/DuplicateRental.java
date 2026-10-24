package kz.narxoz.carrental.domain;

public class DuplicateRental extends RuntimeException {

    public DuplicateRental(String businessKey) {
        super("duplicate rental request: " + businessKey);
    }
}
