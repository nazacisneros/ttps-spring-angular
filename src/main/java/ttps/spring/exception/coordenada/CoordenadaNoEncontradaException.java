package ttps.spring.exception.coordenada;

import ttps.spring.exception.EntityNotFoundException;

public class CoordenadaNoEncontradaException extends EntityNotFoundException {
    public CoordenadaNoEncontradaException(String message) {
        super(message);
    }

    public CoordenadaNoEncontradaException(Long id) {
        super("Coordenada no encontrada con ID: " + id);
    }

    public CoordenadaNoEncontradaException(String message, Throwable cause) {
        super(message, cause);
    }
}
