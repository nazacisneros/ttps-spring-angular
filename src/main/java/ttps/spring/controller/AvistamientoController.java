package ttps.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ttps.spring.entity.Avistamiento;
import ttps.spring.exception.avistamiento.AvistamientoNoEncontradoException;
import ttps.spring.exception.avistamiento.AvistamientoValidationException;
import ttps.spring.model.ErrorResponse;
import ttps.spring.service.GenericService;
import ttps.spring.service.AvistamientoService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/avistamientos")
public class AvistamientoController extends GenericController<Avistamiento, Long> {

    private final AvistamientoService service;

    public AvistamientoController(AvistamientoService service) {
        this.service = service;
    }

    @Override
    protected GenericService<Avistamiento, Long> getService() {
        return service;
    }

    @Override
    protected String getBasePath() {
        return "/api/avistamientos";
    }

    @Override
    protected Long getId(Avistamiento entidad) {
        return entidad.getId();
    }

    @GetMapping
    public List<Avistamiento> listar() {
        return service.listar();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Avistamiento> listarPorUsuario(@PathVariable Long usuarioId) {
        return service.listarPorUsuario(usuarioId);
    }

    @GetMapping("/recientes")
    public List<Avistamiento> listarRecientes() {
        return service.listarRecientes();
    }

    /*
     * @GetMapping("/zona/{zona}")
     * public List<Avistamiento> listarPorZona() { //para desarrollar
     * return service.listarPorZona();
     * }
     * 
     */

    @PostMapping("/usuario/{usuarioId}/mascota/{mascotaId}")
    public ResponseEntity<Avistamiento> crear(
            @PathVariable Long usuarioId,
            @PathVariable Long mascotaId,
            @RequestBody Avistamiento avistamiento) {
        Avistamiento creado = service.crearAvistamiento(usuarioId, mascotaId, avistamiento);
        return ResponseEntity.created(URI.create("/api/avistamientos/" + creado.getId())).body(creado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Avistamiento> obtener(@PathVariable Long id) {
        return service.obtener(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Exception Handlers
    @ExceptionHandler(AvistamientoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleAvistamientoNoEncontrado(AvistamientoNoEncontradoException e) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(AvistamientoValidationException.class)
    public ResponseEntity<ErrorResponse> handleAvistamientoValidation(AvistamientoValidationException e) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}