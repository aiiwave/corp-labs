package kz.narxoz.carrental.handler;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import kz.narxoz.carrental.domain.Rental;

public record RentalResponse(
        UUID id,
        String businessKey,
        @Schema(example = "REQUESTED") String status,
        String title) {

    public static RentalResponse from(Rental rental) {
        return new RentalResponse(rental.id(), rental.businessKey(), rental.status().name(), rental.title());
    }
}
