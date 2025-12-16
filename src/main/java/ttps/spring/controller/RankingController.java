package ttps.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ttps.spring.entity.Ranking;
import ttps.spring.exception.ranking.RankingNoEncontradoException;
import ttps.spring.model.ErrorResponse;
import ttps.spring.service.GenericService;
import ttps.spring.service.RankingService;

@RestController
@RequestMapping("/api/rankings")
public class RankingController extends GenericController<Ranking, Long> {

    private final RankingService service;

    public RankingController(RankingService service) {
        this.service = service;
    }

    @Override
    protected GenericService<Ranking, Long> getService() {
        return service;
    }

    @Override
    protected String getBasePath() {
        return "/api/rankings";
    }

    @Override
    protected Long getId(Ranking entidad) {
        return entidad.getId();
    }

    // Exception Handlers
    @ExceptionHandler(RankingNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleRankingNoEncontrado(RankingNoEncontradoException e) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}