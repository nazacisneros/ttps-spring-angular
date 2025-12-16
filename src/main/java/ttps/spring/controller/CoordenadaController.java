package ttps.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ttps.spring.entity.Coordenada;
import ttps.spring.exception.coordenada.CoordenadaNoEncontradaException;
import ttps.spring.model.ErrorResponse;
import ttps.spring.service.GenericService;
import ttps.spring.service.CoordenadaService;

@RestController
@RequestMapping("/api/coordenadaes")
public class CoordenadaController extends GenericController<Coordenada, Long> {

    private final CoordenadaService service;

    public CoordenadaController(CoordenadaService service) {
        this.service = service;
    }

    @Override
    protected GenericService<Coordenada, Long> getService() {
        return service;
    }

    @Override
    protected String getBasePath() {
        return "/api/coordenadaes";
    }

    @Override
    protected Long getId(Coordenada entidad) {
        return entidad.getId();
    }

    // Exception Handlers
    @ExceptionHandler(CoordenadaNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleCoordenadaNoEncontrada(CoordenadaNoEncontradaException e) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}