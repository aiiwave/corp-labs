package kz.narxoz.carrental;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RentalPolicyTest {

    private final RentalPolicy policy = new RentalPolicy();

    // The two allowed rows from the README status table.
    @ParameterizedTest
    @CsvSource({
            "REQUESTED, APPROVED, APPROVED",
            "APPROVED,  RENTED,   RENTED"
    })
    void allowedTransitionsReturnNewStatus(RentalStatus from, RentalStatus to, RentalStatus expected) {
        assertEquals(expected, policy.move(from, to));
    }

    // The two forbidden rows from the README status table.
    @ParameterizedTest
    @CsvSource({
            "REQUESTED, RENTED",
            "RENTED,    REQUESTED"
    })
    void forbiddenTransitionsThrow(RentalStatus from, RentalStatus to) {
        assertThrows(IllegalStateException.class, () -> policy.move(from, to));
    }

    @Test
    void nullIdIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new RentalId(null));
    }

    @Test
    void blankIdIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new RentalId(" "));
    }
}
