package ttps.spring.exception.publicacion;

import ttps.spring.exception.EntityValidationException;

public class PublicacionValidationException extends EntityValidationException {
    public PublicacionValidationException(String message) {
        super(message);
    }

    public PublicacionValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
