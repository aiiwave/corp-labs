package kz.narxoz.carrental.handler;

/** The only error body on the wire. */
public record ApiError(String code, String message, String correlationId) {
}
