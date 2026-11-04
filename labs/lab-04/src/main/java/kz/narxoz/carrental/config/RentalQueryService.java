package kz.narxoz.carrental.config;

import java.util.UUID;
import kz.narxoz.carrental.domain.Rental;
import kz.narxoz.carrental.domain.RentalNotFound;
import kz.narxoz.carrental.persistence.RentalJpa;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Read side. It never registers, moves or flushes. */
@Service
public class RentalQueryService {

    private final RentalJpa rentals;

    public RentalQueryService(RentalJpa rentals) {
        this.rentals = rentals;
    }

    @Transactional(readOnly = true)
    public Rental get(UUID id) {
        return rentals.findById(id).orElseThrow(() -> new RentalNotFound(id.toString()));
    }
}
