package kz.narxoz.carrental;

/**
 * Identifier of a rental request.
 * It cannot be null or blank.
 */
public final class RentalId {

    private final String value;

    public RentalId(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Rental id cannot be null");
        }
        if (value.isBlank()) {
            throw new IllegalArgumentException("Rental id cannot be blank");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
