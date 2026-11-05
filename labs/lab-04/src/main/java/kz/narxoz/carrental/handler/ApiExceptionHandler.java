package kz.narxoz.carrental.handler;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.UUID;
import kz.narxoz.carrental.domain.DuplicateRental;
import kz.narxoz.carrental.domain.RentalNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/** The only place that knows 400, 404, 409, 415, 500. Body is always code, message, correlationId. */
@RestControllerAdvice
public class ApiExceptionHandler {

    public static final String CORRELATION_HEADER = "X-Correlation-Id";

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .sorted(Comparator.comparing(FieldError::getField))
                .findFirst()
                .map(e -> words(e.getField()) + " " + e.getDefaultMessage())
                .orElse("request is not valid");
        return error(HttpStatus.BAD_REQUEST, "VALIDATION", message, req);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> badPathValue(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        String message = UUID.class.equals(ex.getRequiredType())
                ? ex.getName() + " must be a UUID"
                : ex.getName() + " is invalid";
        return error(HttpStatus.BAD_REQUEST, "VALIDATION", message, req);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> notJson(HttpServletRequest req) {
        return error(HttpStatus.BAD_REQUEST, "VALIDATION", "body must be JSON", req);
    }

    @ExceptionHandler(RentalNotFound.class)
    public ResponseEntity<ApiError> notFound(RentalNotFound ex, HttpServletRequest req) {
        return error(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage(), req);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> noResource(HttpServletRequest req) {
        return error(HttpStatus.NOT_FOUND, "NOT_FOUND", "resource not found", req);
    }

    @ExceptionHandler(DuplicateRental.class)
    public ResponseEntity<ApiError> duplicate(DuplicateRental ex, HttpServletRequest req) {
        return error(HttpStatus.CONFLICT, "DUPLICATE", ex.getMessage(), req);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiError> unsupported(HttpServletRequest req) {
        return error(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "UNSUPPORTED", "unsupported media type", req);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> wrongMethod(HttpServletRequest req) {
        return error(HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED", "method not allowed", req);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> unexpected(Exception ex, HttpServletRequest req) {
        String correlationId = correlationId(req);
        log.error("unexpected error, correlationId={}", correlationId, ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError("INTERNAL", "unexpected error", correlationId));
    }

    private static ResponseEntity<ApiError> error(HttpStatus status, String code, String message,
                                                  HttpServletRequest req) {
        return ResponseEntity.status(status).body(new ApiError(code, message, correlationId(req)));
    }

    /** Caller's id if present, otherwise a fresh one. Week 13 moves this into a filter. */
    private static String correlationId(HttpServletRequest req) {
        String header = req.getHeader(CORRELATION_HEADER);
        return header == null || header.isBlank() ? UUID.randomUUID().toString() : header.strip();
    }

    /** businessKey -> business key */
    private static String words(String field) {
        return field.replaceAll("([a-z])([A-Z])", "$1 $2").toLowerCase();
    }
}
