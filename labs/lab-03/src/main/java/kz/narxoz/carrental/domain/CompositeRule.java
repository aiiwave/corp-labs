package kz.narxoz.carrental.domain;

import java.util.List;

public class CompositeRule implements Rule {

    private final List<Rule> rules;

    public CompositeRule(List<Rule> rules) {
        this.rules = List.copyOf(rules);
    }

    @Override
    public void check(RentalStatus from, RentalStatus to) {
        for (Rule rule : rules) {
            rule.check(from, to);
        }
    }
}
