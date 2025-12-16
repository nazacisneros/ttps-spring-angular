package ttps.spring.exception.ciudad;

import ttps.spring.exception.EntityNotFoundException;

public class CiudadNoEncontradaException extends EntityNotFoundException {
    public CiudadNoEncontradaException(String message) {
        super(message);
    }

    public CiudadNoEncontradaException(Long id) {
        super("Ciudad no encontrada con ID: " + id);
    }

    public CiudadNoEncontradaException(String message, Throwable cause) {
        super(message, cause);
    }
}
