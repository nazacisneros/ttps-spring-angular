package ttps.spring.exception.auth;

public class CredencialesInvalidasException extends RuntimeException {
    public CredencialesInvalidasException(String message) {
        super(message);
    }

    public CredencialesInvalidasException(String message, Throwable cause) {
        super(message, cause);
    }
}