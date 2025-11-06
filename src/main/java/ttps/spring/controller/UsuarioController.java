package ttps.spring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ttps.spring.entity.Usuario;
import ttps.spring.service.GenericService;
import ttps.spring.service.UsuarioService;

import java.net.URI;
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
        try {
            Usuario actualizado = service.editarPerfil(id, usuario);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/deshabilitar")
    public ResponseEntity<Void> deshabilitar(@PathVariable Long id) {
        try {
            service.deshabilitarUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/habilitar")
    public ResponseEntity<Void> habilitar(@PathVariable Long id) {
        try {
            service.habilitarUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    //  endpoints especificos de Usuario
    /*@GetMapping("/activos")
    public List<Usuario> listarActivos() {
    }
     */
}