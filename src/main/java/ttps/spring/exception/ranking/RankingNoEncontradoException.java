package ttps.spring.exception.ranking;

import ttps.spring.exception.EntityNotFoundException;

public class RankingNoEncontradoException extends EntityNotFoundException {
    public RankingNoEncontradoException(String message) {
        super(message);
    }

    public RankingNoEncontradoException(Long id) {
        super("Ranking no encontrado con ID: " + id);
    }

    public RankingNoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }
}
