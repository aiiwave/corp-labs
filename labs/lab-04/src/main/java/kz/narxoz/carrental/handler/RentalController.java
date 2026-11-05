package kz.narxoz.carrental.handler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import kz.narxoz.carrental.config.RentalQueryService;
import kz.narxoz.carrental.config.RentalRegistrationService;
import kz.narxoz.carrental.domain.RentalId;
import kz.narxoz.carrental.domain.RentalStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/** HTTP adapter. Chooses the status; the services still insert and read. */
@RestController
@RequestMapping("/api/v1/rental-requests")
public class RentalController {

    private final RentalRegistrationService registration;
    private final RentalQueryService query;

    public RentalController(RentalRegistrationService registration, RentalQueryService query) {
        this.registration = registration;
        this.query = query;
    }

    @Operation(summary = "Create a rental request")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(schema = @Schema(implementation = RentalResponse.class))),
            @ApiResponse(responseCode = "400", description = "VALIDATION",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "DUPLICATE: business key already stored",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RentalResponse> create(@Valid @RequestBody RentalRequest request) {
        RentalId id = RentalId.newId();
        registration.register(id, request.businessKey(), request.title());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(id.value()).toUri();
        RentalResponse body = new RentalResponse(
                id.toUuid(), request.businessKey(), RentalStatus.REQUESTED.name(), request.title());
        return ResponseEntity.created(location).body(body);
    }

    @Operation(summary = "Read a rental request by its UUID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found",
                    content = @Content(schema = @Schema(implementation = RentalResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{id}")
    public RentalResponse get(@PathVariable UUID id) {
        return RentalResponse.from(query.get(id));
    }
}
