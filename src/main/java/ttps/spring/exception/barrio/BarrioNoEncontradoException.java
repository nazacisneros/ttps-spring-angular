package ttps.spring.exception.barrio;

import ttps.spring.exception.EntityNotFoundException;

public class BarrioNoEncontradoException extends EntityNotFoundException {
    public BarrioNoEncontradoException(String message) {
        super(message);
    }

    public BarrioNoEncontradoException(Long id) {
        super("Barrio no encontrado con ID: " + id);
    }

    public BarrioNoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }
}
