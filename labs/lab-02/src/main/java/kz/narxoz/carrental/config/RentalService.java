package kz.narxoz.carrental.config;

import kz.narxoz.carrental.domain.RentalStatus;
import kz.narxoz.carrental.domain.Rule;
import org.springframework.stereotype.Service;

/**
 * Spring-managed service. Injects the composed Rule bean.
 * Contains no business logic itself — it only delegates to Rule.
 */
@Service
public class RentalService {

    private final Rule rules;

    public RentalService(Rule rules) {
        this.rules = rules;
    }

    public RentalStatus move(RentalStatus from, RentalStatus to) {
        rules.check(from, to);
        return to;
    }
}
