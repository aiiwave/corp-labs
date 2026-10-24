package kz.narxoz.carrental.domain;

public interface Rule {
    /** Returns normally if the move is allowed, throws IllegalStateException if not. */
    void check(RentalStatus from, RentalStatus to);
}
