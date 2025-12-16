package ttps.spring.exception.usuario;

public class UsuarioOperationException extends RuntimeException {
    public UsuarioOperationException(String message) {
        super(message);
    }

    public UsuarioOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
