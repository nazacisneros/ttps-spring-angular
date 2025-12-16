package ttps.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ttps.spring.entity.Usuario;
import ttps.spring.exception.auth.UsuarioNoEncontradoException;
import ttps.spring.exception.usuario.UsuarioOperationException;
import ttps.spring.exception.usuario.UsuarioValidationException;
import ttps.spring.model.ErrorResponse;
import ttps.spring.service.GenericService;
import ttps.spring.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController extends GenericController<Usuario, Long> {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @Override
    protected GenericService<Usuario, Long> getService() {
        return service;
    }

    @GetMapping
    public List<Usuario> listar() {
        return service.listar();
    }

    @Override
    protected String getBasePath() {
        return "/api/usuarios";
    }

    @Override
    protected Long getId(Usuario entidad) {
        return entidad.getId();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtener(@PathVariable Long id) {
        return service.obtener(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/perfil")
    public ResponseEntity<Usuario> editarPerfil(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario actualizado = service.editarPerfil(id, usuario);
        return ResponseEntity.ok(actualizado);
    }

    @PutMapping("/{id}/deshabilitar")
    public ResponseEntity<Void> deshabilitar(@PathVariable Long id) {
        service.deshabilitarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/habilitar")
    public ResponseEntity<Void> habilitar(@PathVariable Long id) {
        service.habilitarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    // endpoints especificos de Usuario
    /*
     * @GetMapping("/activos")
     * public List<Usuario> listarActivos() {
     * }
     */

    // Exception Handlers
    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioNoEncontrado(UsuarioNoEncontradoException e) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UsuarioValidationException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioValidation(UsuarioValidationException e) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(UsuarioOperationException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioOperation(UsuarioOperationException e) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}