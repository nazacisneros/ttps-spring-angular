package ttps.spring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ttps.spring.service.GenericService;

import java.net.URI;
import java.util.List;

public abstract class GenericController<T, ID> {

    protected abstract GenericService<T, ID> getService();
    protected abstract String getBasePath();

    @GetMapping
    public List<T> listar() {
        return getService().listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<T> obtener(@PathVariable ID id) {
        return getService().obtener(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<T> crear(@RequestBody T entidad) {
        T creado = getService().crear(entidad);
        return ResponseEntity.created(URI.create(getBasePath() + "/" + getId(creado))).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<T> actualizar(@PathVariable ID id, @RequestBody T entidad) {
        try {
            T actualizado = getService().actualizar(id, entidad);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable ID id) {
        try {
            getService().eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    protected abstract ID getId(T entidad);
}