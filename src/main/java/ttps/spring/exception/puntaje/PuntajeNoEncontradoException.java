package ttps.spring.exception.puntaje;

import ttps.spring.exception.EntityNotFoundException;

public class PuntajeNoEncontradoException extends EntityNotFoundException {
    public PuntajeNoEncontradoException(String message) {
        super(message);
    }

    public PuntajeNoEncontradoException(Long id) {
        super("Puntaje no encontrado con ID: " + id);
    }

    public PuntajeNoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }
}
