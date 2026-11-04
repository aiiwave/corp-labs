package kz.narxoz.carrental.handler;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RentalRequest(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "RR-101")
        @NotBlank(message = "must not be blank")
        String businessKey,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "Toyota Camry, 3 days", maxLength = 120)
        @NotBlank(message = "must not be blank")
        @Size(max = 120, message = "size must be between 0 and 120")
        String title) {
}
