package ttps.spring.exception.usuario;

import ttps.spring.exception.EntityValidationException;

public class UsuarioValidationException extends EntityValidationException {
    public UsuarioValidationException(String message) {
        super(message);
    }

    public UsuarioValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
