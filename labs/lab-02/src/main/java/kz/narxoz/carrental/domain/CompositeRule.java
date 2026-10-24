package kz.narxoz.carrental.domain;

import java.util.List;

/**
 * Chains several Rule implementations together.
 * Every rule must pass; the first failure stops the check.
 */
public class CompositeRule implements Rule {

    private final List<Rule> rules;

    public CompositeRule(List<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public void check(RentalStatus from, RentalStatus to) {
        for (Rule rule : rules) {
            rule.check(from, to);
        }
    }
}
