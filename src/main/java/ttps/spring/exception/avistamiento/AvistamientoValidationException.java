package ttps.spring.exception.avistamiento;

import ttps.spring.exception.EntityValidationException;

public class AvistamientoValidationException extends EntityValidationException {
    public AvistamientoValidationException(String message) {
        super(message);
    }

    public AvistamientoValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
