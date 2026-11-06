package kz.narxoz.carrental.domain;

import java.util.UUID;

public final class RentalId {

    private final String value;

    public RentalId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("RentalId must not be null or blank");
        }
        this.value = value;
    }

    public static RentalId newId() {
        return new RentalId(UUID.randomUUID().toString());
    }

    public String value() {
        return value;
    }

    public UUID toUuid() {
        return UUID.fromString(value);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RentalId other && value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
