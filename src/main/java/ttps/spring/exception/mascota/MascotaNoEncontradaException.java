package ttps.spring.exception.mascota;

import ttps.spring.exception.EntityNotFoundException;

public class MascotaNoEncontradaException extends EntityNotFoundException {
    public MascotaNoEncontradaException(String message) {
        super(message);
    }

    public MascotaNoEncontradaException(Long id) {
        super("Mascota no encontrada con ID: " + id);
    }

    public MascotaNoEncontradaException(String message, Throwable cause) {
        super(message, cause);
    }
}
