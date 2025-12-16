package ttps.spring.exception.mascota;

public class MascotaOperationException extends RuntimeException {
    public MascotaOperationException(String message) {
        super(message);
    }

    public MascotaOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
