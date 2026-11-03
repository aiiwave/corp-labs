package kz.narxoz.carrental.domain;

import java.util.UUID;

/** A stored rental request as the domain sees it. No persistence, no web. */
public record Rental(UUID id, String businessKey, RentalStatus status, String title) {
}
