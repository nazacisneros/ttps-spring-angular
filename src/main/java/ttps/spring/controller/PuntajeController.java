package ttps.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ttps.spring.entity.Puntaje;
import ttps.spring.exception.puntaje.PuntajeNoEncontradoException;
import ttps.spring.model.ErrorResponse;
import ttps.spring.service.GenericService;
import ttps.spring.service.PuntajeService;

@RestController
@RequestMapping("/api/puntajes")
public class PuntajeController extends GenericController<Puntaje, Long> {

    private final PuntajeService service;

    public PuntajeController(PuntajeService service) {
        this.service = service;
    }

    @Override
    protected GenericService<Puntaje, Long> getService() {
        return service;
    }

    @Override
    protected String getBasePath() {
        return "/api/puntajes";
    }

    @Override
    protected Long getId(Puntaje entidad) {
        return entidad.getId();
    }

    // Exception Handlers
    @ExceptionHandler(PuntajeNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handlePuntajeNoEncontrado(PuntajeNoEncontradoException e) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}