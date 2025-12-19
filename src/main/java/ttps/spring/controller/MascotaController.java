package ttps.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ttps.spring.entity.Mascota;
import ttps.spring.entity.Usuario;
import ttps.spring.exception.mascota.MascotaNoEncontradaException;
import ttps.spring.exception.mascota.MascotaOperationException;
import ttps.spring.exception.mascota.MascotaValidationException;
import ttps.spring.model.ErrorResponse;
import ttps.spring.service.GenericService;
import ttps.spring.service.MascotaService;
import ttps.spring.service.UsuarioService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/Mascotas")
public class MascotaController extends GenericController<Mascota, Long> {

    private final MascotaService service;
    private final UsuarioService usuarioService;

    public MascotaController(MascotaService service, UsuarioService usuarioService) {
        this.service = service;
        this.usuarioService = usuarioService;
    }

    @Override
    protected GenericService<Mascota, Long> getService() {
        return service;
    }

    @Override
    protected String getBasePath() {
        return "/api/Mascotas";
    }

    @Override
    protected Long getId(Mascota entidad) {
        return entidad.getId();
    }

    @GetMapping
    public List<Mascota> listar() {
        return service.listar();
    }

    @GetMapping("/perdidas")
    public List<Mascota> listarPerdidas() {
        return service.listarPerdidas();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Mascota> listarPorUsuario(@PathVariable Long usuarioId) {
        return service.listarPorUsuario(usuarioId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mascota> obtener(@PathVariable Long id) {
        return service.obtener(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/usuario/{usuarioId}", consumes = "application/json")
    public ResponseEntity<Mascota> crear(@PathVariable Long usuarioId, @RequestBody Mascota mascota) {
        Mascota creada = service.crearMascota(usuarioId, mascota);
        return ResponseEntity.created(URI.create("/api/mascotas/" + creada.getId())).body(creada);
    }

    @PutMapping("/{id}/usuario/{usuarioId}")
    public ResponseEntity<Mascota> editar(
            @PathVariable Long id,
            @PathVariable Long usuarioId,
            @RequestBody Mascota mascota) {
        Mascota actualizada = service.editarMascota(usuarioId, id, mascota);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}/usuario/{usuarioId}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, @PathVariable Long usuarioId,
            Authentication authentication) {

        String email = authentication.getName();
        Usuario usuarioAutenticado = usuarioService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        service.eliminarMascota(usuarioAutenticado.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/perdida")
    public ResponseEntity<Mascota> marcarPerdida(@PathVariable Long id) {
        Mascota mascota = service.marcarComoPerdida(id);
        return ResponseEntity.ok(mascota);
    }

    @PutMapping("/{id}/encontrada")
    public ResponseEntity<Mascota> marcarEncontrada(@PathVariable Long id) {
        Mascota mascota = service.marcarComoEncontrada(id);
        return ResponseEntity.ok(mascota);
    }

    @ExceptionHandler(MascotaNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleMascotaNoEncontrada(MascotaNoEncontradaException e) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MascotaValidationException.class)
    public ResponseEntity<ErrorResponse> handleMascotaValidation(MascotaValidationException e) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MascotaOperationException.class)
    public ResponseEntity<ErrorResponse> handleMascotaOperation(MascotaOperationException e) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}