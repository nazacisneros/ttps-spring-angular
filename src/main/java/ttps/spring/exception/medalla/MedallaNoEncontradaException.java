package ttps.spring.exception.medalla;

import ttps.spring.exception.EntityNotFoundException;

public class MedallaNoEncontradaException extends EntityNotFoundException {
    public MedallaNoEncontradaException(String message) {
        super(message);
    }

    public MedallaNoEncontradaException(Long id) {
        super("Medalla no encontrada con ID: " + id);
    }

    public MedallaNoEncontradaException(String message, Throwable cause) {
        super(message, cause);
    }
}
