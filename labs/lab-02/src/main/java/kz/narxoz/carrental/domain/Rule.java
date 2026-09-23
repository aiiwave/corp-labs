package kz.narxoz.carrental.domain;

/**
 * A business rule that either allows a status transition silently,
 * or throws if the transition breaks the rule.
 * No org.springframework import here — domain stays plain Java.
 */
public interface Rule {
    void check(RentalStatus from, RentalStatus to);
}
