package kz.narxoz.carrental.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RuleTest {

    private final Rule rule = new CompositeRule(List.of(
            new TerminalStatusRule(),
            new TransitionRule()
    ));

    @Test
    void allowedTransitionPasses() {
        assertDoesNotThrow(() -> rule.check(RentalStatus.REQUESTED, RentalStatus.APPROVED));
    }

    @Test
    void forbiddenTransitionThrows() {
        assertThrows(IllegalStateException.class,
                () -> rule.check(RentalStatus.REQUESTED, RentalStatus.RENTED));
    }

    @Test
    void terminalStatusBlocksAnyFurtherMove() {
        assertThrows(IllegalStateException.class,
                () -> rule.check(RentalStatus.RENTED, RentalStatus.REQUESTED));
    }
}
