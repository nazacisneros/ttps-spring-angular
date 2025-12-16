package ttps.spring.exception.mascota;

import ttps.spring.exception.EntityValidationException;

public class MascotaValidationException extends EntityValidationException {
    public MascotaValidationException(String message) {
        super(message);
    }

    public MascotaValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
