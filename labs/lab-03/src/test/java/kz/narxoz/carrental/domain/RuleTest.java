package kz.narxoz.carrental.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

class RuleTest {

    private final Rule rule = new CompositeRule(List.of(new TerminalStatusRule(), new TransitionRule()));

    @Test
    void allowedPasses() {
        assertDoesNotThrow(() -> rule.check(RentalStatus.REQUESTED, RentalStatus.APPROVED));
    }

    @Test
    void forbiddenByTable() {
        assertThrows(IllegalStateException.class,
                () -> rule.check(RentalStatus.REQUESTED, RentalStatus.RENTED));
    }

    @Test
    void rentedIsFinal() {
        assertThrows(IllegalStateException.class,
                () -> rule.check(RentalStatus.RENTED, RentalStatus.APPROVED));
    }
}
