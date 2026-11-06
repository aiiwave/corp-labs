package kz.narxoz.carrental.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.UUID;
import kz.narxoz.carrental.config.Application;
import kz.narxoz.carrental.config.RentalStatusService;
import kz.narxoz.carrental.domain.RentalNotFound;
import kz.narxoz.carrental.domain.RentalStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;

/** Real PostgreSQL, no @Transactional on the test: we read committed data. */
@SpringBootTest(classes = Application.class)
class RentalStatusPersistenceTest {

    @Autowired
    private RentalStatusService statusService;
    @Autowired
    private RentalJdbc rentals;
    @Autowired
    private JdbcTemplate jdbc;

    @BeforeEach
    void cleanDatabase() {
        jdbc.execute("DELETE FROM rental_request");
    }

    @Test
    void allowedMoveIsSaved() {
        rentals.insert(UUID.randomUUID(), "RR-201", RentalStatus.REQUESTED, "Toyota Camry");
        statusService.move("RR-201", RentalStatus.APPROVED);
        assertEquals(Optional.of(RentalStatus.APPROVED), rentals.findStatus("RR-201"));
    }

    @Test
    void forbiddenMoveChangesNothing() {
        rentals.insert(UUID.randomUUID(), "RR-202", RentalStatus.REQUESTED, "Kia Rio");
        assertThrows(IllegalStateException.class,
                () -> statusService.move("RR-202", RentalStatus.RENTED));
        assertEquals(Optional.of(RentalStatus.REQUESTED), rentals.findStatus("RR-202"));
    }

    @Test
    void rentedStaysRented() {
        rentals.insert(UUID.randomUUID(), "RR-203", RentalStatus.RENTED, "Hyundai Solaris");
        assertThrows(IllegalStateException.class,
                () -> statusService.move("RR-203", RentalStatus.APPROVED));
        assertEquals(Optional.of(RentalStatus.RENTED), rentals.findStatus("RR-203"));
    }

    @Test
    void unknownRequestThrows() {
        assertThrows(RentalNotFound.class, () -> statusService.move("RR-999", RentalStatus.APPROVED));
    }

    @Test
    void jdbcDuplicateKeyIsRejected() {
        rentals.insert(UUID.randomUUID(), "RR-204", RentalStatus.REQUESTED, "First");
        assertThrows(DataIntegrityViolationException.class,
                () -> rentals.insert(UUID.randomUUID(), "RR-204", RentalStatus.REQUESTED, "Second"));
        assertEquals(1, rentals.count("RR-204"));
    }
}
