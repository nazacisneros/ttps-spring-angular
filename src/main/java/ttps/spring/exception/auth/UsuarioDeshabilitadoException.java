package ttps.spring.exception.auth;

public class UsuarioDeshabilitadoException extends RuntimeException {
    public UsuarioDeshabilitadoException(String message) {
        super(message);
    }

    public UsuarioDeshabilitadoException(String message, Throwable cause) {
        super(message, cause);
    }
}