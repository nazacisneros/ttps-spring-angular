package ttps.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ttps.spring.entity.Ciudad;
import ttps.spring.exception.ciudad.CiudadNoEncontradaException;
import ttps.spring.model.ErrorResponse;
import ttps.spring.service.GenericService;
import ttps.spring.service.CiudadService;

@RestController
@RequestMapping("/api/ciudades")
public class CiudadController extends GenericController<Ciudad, Long> {

    private final CiudadService service;

    public CiudadController(CiudadService service) {
        this.service = service;
    }

    @Override
    protected GenericService<Ciudad, Long> getService() {
        return service;
    }

    @Override
    protected String getBasePath() {
        return "/api/Ciudades";
    }

    @Override
    protected Long getId(Ciudad entidad) {
        return entidad.getId();
    }

    @ExceptionHandler(CiudadNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleCiudadNoEncontrada(CiudadNoEncontradaException e) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}