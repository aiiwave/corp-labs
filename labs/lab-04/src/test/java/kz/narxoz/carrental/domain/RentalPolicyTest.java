package kz.narxoz.carrental.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RentalPolicyTest {

    private final RentalPolicy policy = new RentalPolicy();

    @ParameterizedTest
    @CsvSource({
            "REQUESTED, APPROVED,  true",
            "APPROVED,  RENTED,    true",
            "REQUESTED, RENTED,    false",
            "RENTED,    REQUESTED, false"
    })
    void statusTable(RentalStatus from, RentalStatus to, boolean allowed) {
        if (allowed) {
            assertEquals(to, policy.move(from, to));
        } else {
            assertThrows(IllegalStateException.class, () -> policy.move(from, to));
        }
    }

    @Test
    void nullIdThrows() {
        assertThrows(IllegalArgumentException.class, () -> new RentalId(null));
    }

    @Test
    void blankIdThrows() {
        assertThrows(IllegalArgumentException.class, () -> new RentalId("  "));
    }
}
