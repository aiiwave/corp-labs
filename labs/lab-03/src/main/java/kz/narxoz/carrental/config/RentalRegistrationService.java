package kz.narxoz.carrental.config;

import java.util.UUID;
import kz.narxoz.carrental.domain.DuplicateRental;
import kz.narxoz.carrental.domain.RentalId;
import kz.narxoz.carrental.domain.RentalStatus;
import kz.narxoz.carrental.persistence.RentalJdbc;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RentalRegistrationService {

    private final RentalJdbc rentals;

    public RentalRegistrationService(RentalJdbc rentals) {
        this.rentals = rentals;
    }

    @Transactional
    public void register(RentalId id, String businessKey, String title) {
        requireText(businessKey, "businessKey");
        requireText(title, "title");
        try {
            rentals.insert(id.toUuid(), businessKey, RentalStatus.REQUESTED.name(), title);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateRental(businessKey);
        }
    }

    @Transactional
    public void insertTwice(String businessKey) {
        requireText(businessKey, "businessKey");
        rentals.insert(UUID.randomUUID(), businessKey, RentalStatus.REQUESTED.name(), "Toyota Camry, 3 days");
        try {
            rentals.insert(UUID.randomUUID(), businessKey, RentalStatus.REQUESTED.name(), "Toyota Camry again");
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateRental(businessKey); // unchecked -> rollback
        }
    }

    private static void requireText(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be null or blank");
        }
    }
}
