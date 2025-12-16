package ttps.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ttps.spring.exception.EntityNotFoundException;
import ttps.spring.model.ErrorResponse;
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
        T actualizado = getService().actualizar(id, entidad);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable ID id) {
        getService().eliminar(id);
        return ResponseEntity.noContent().build();
    }

    protected abstract ID getId(T entidad);

    // Generic Exception Handler
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException e) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}