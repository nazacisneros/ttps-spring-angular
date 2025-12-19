package ttps.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ttps.spring.entity.Barrio;
import ttps.spring.entity.Ciudad;
import ttps.spring.exception.barrio.BarrioNoEncontradoException;
import ttps.spring.model.ErrorResponse;
import ttps.spring.service.GenericService;
import ttps.spring.service.BarrioService;
import ttps.spring.service.CiudadService;
import java.util.List;

@RestController
@RequestMapping("/api/barrios")
public class BarrioController extends GenericController<Barrio, Long> {

    private final BarrioService service;
    private final CiudadService ciudadService;

    public BarrioController(BarrioService service, CiudadService ciudadService) {
        this.service = service;
        this.ciudadService = ciudadService;
    }

    @Override
    protected GenericService<Barrio, Long> getService() {
        return service;
    }

    @Override
    protected String getBasePath() {
        return "/api/barrios";
    }

    @Override
    protected Long getId(Barrio entidad) {
        return entidad.getId();
    }

    @GetMapping("/ciudad/{ciudadId}")
    public ResponseEntity<List<Barrio>> getBarriosByCiudad(@PathVariable Long ciudadId) {
        Ciudad ciudad = ciudadService.obtener(ciudadId).orElse(null);
        if (ciudad == null) {
            return ResponseEntity.notFound().build();
        }
        List<Barrio> barrios = service.findByCiudad(ciudad);
        return ResponseEntity.ok(barrios);
    }

    // Exception Handlers
    @ExceptionHandler(BarrioNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleBarrioNoEncontradoException(BarrioNoEncontradoException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
