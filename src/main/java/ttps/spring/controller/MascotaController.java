package ttps.spring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ttps.spring.entity.Mascota;
import ttps.spring.service.GenericService;
import ttps.spring.service.MascotaService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/Mascotas")
public class MascotaController extends GenericController<Mascota, Long> {

    private final MascotaService service;

    public MascotaController(MascotaService service) {
        this.service = service;
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

    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<Mascota> crear(@PathVariable Long usuarioId, @RequestBody Mascota mascota) {
        Mascota creada = service.crearMascota(usuarioId, mascota);
        return ResponseEntity.created(URI.create("/api/mascotas/" + creada.getId())).body(creada);
    }

    @PutMapping("/{id}/usuario/{usuarioId}")
    public ResponseEntity<Mascota> editar(
            @PathVariable Long id,
            @PathVariable Long usuarioId,
            @RequestBody Mascota mascota) {
        try {
            Mascota actualizada = service.editarMascota(usuarioId, id, mascota);
            return ResponseEntity.ok(actualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}/usuario/{usuarioId}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, @PathVariable Long usuarioId) {
        try {
            service.eliminarMascota(usuarioId, id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/perdida")
    public ResponseEntity<Mascota> marcarPerdida(@PathVariable Long id) {
        try {
            Mascota mascota = service.marcarComoPerdida(id);
            return ResponseEntity.ok(mascota);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/encontrada")
    public ResponseEntity<Mascota> marcarEncontrada(@PathVariable Long id) {
        try {
            Mascota mascota = service.marcarComoEncontrada(id);
            return ResponseEntity.ok(mascota);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}