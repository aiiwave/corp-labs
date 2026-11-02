package kz.narxoz.carrental.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.UUID;
import kz.narxoz.carrental.config.Application;
import kz.narxoz.carrental.config.RentalRegistrationService;
import kz.narxoz.carrental.domain.DuplicateRental;
import kz.narxoz.carrental.domain.RentalId;
import kz.narxoz.carrental.domain.RentalStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(classes = Application.class)
class RentalPersistenceTest {

    @Autowired
    private RentalRegistrationService service;
    @Autowired
    private RentalJpa rentals;
    @Autowired
    private JdbcTemplate jdbc;

    @BeforeEach
    void cleanDatabase() {
        jdbc.execute("DELETE FROM rental_request");
    }

    @Test
    void secondStatementRollsBack() {
        assertThrows(DuplicateRental.class, () -> service.insertTwice("RR-101"));
        assertEquals(0, rentals.count("RR-101"));
    }

    @Test
    void secondRequestKeepsTheFirst() {
        service.register(RentalId.newId(), "RR-101", "Toyota Camry, 3 days");
        assertThrows(DuplicateRental.class,
                () -> service.register(RentalId.newId(), "RR-101", "Toyota Camry again"));
        assertEquals(1, rentals.count("RR-101"));
    }

    @Test
    void statusIsStoredAndReadBack() {
        service.register(RentalId.newId(), "RR-102", "Kia Rio, weekend");
        assertEquals(Optional.of(RentalStatus.REQUESTED), rentals.findStatus("RR-102"));
    }

    @Test
    void databaseRejectsUnknownStatus() {
        assertThrows(DataIntegrityViolationException.class,
                () -> rentals.insert(UUID.randomUUID(), "RR-103", "LOST", "Bad status"));
    }

    @Test
    void blankBusinessKeyIsRejectedInJava() {
        assertThrows(IllegalArgumentException.class,
                () -> service.register(RentalId.newId(), "  ", "Title"));
    }
}
