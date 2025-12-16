package ttps.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ttps.spring.entity.Publicacion;
import ttps.spring.exception.publicacion.PublicacionNoEncontradaException;
import ttps.spring.exception.publicacion.PublicacionValidationException;
import ttps.spring.model.ErrorResponse;
import ttps.spring.service.GenericService;
import ttps.spring.service.PublicacionService;

@RestController
@RequestMapping("/api/publicaciones")
public class PublicacionController extends GenericController<Publicacion, Long> {

    private final PublicacionService service;

    public PublicacionController(PublicacionService service) {
        this.service = service;
    }

    @Override
    protected GenericService<Publicacion, Long> getService() {
        return service;
    }

    @Override
    protected String getBasePath() {
        return "/api/publicaciones";
    }

    @Override
    protected Long getId(Publicacion entidad) {
        return entidad.getId();
    }

    // Exception Handlers
    @ExceptionHandler(PublicacionNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handlePublicacionNoEncontrada(PublicacionNoEncontradaException e) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(PublicacionValidationException.class)
    public ResponseEntity<ErrorResponse> handlePublicacionValidation(PublicacionValidationException e) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}