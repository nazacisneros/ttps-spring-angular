package ttps.spring.exception.publicacion;

import ttps.spring.exception.EntityNotFoundException;

public class PublicacionNoEncontradaException extends EntityNotFoundException {
    public PublicacionNoEncontradaException(String message) {
        super(message);
    }

    public PublicacionNoEncontradaException(Long id) {
        super("Publicación no encontrada con ID: " + id);
    }

    public PublicacionNoEncontradaException(String message, Throwable cause) {
        super(message, cause);
    }
}
