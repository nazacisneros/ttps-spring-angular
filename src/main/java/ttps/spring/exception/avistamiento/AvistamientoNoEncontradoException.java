package ttps.spring.exception.avistamiento;

import ttps.spring.exception.EntityNotFoundException;

public class AvistamientoNoEncontradoException extends EntityNotFoundException {
    public AvistamientoNoEncontradoException(String message) {
        super(message);
    }

    public AvistamientoNoEncontradoException(Long id) {
        super("Avistamiento no encontrado con ID: " + id);
    }

    public AvistamientoNoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }
}
