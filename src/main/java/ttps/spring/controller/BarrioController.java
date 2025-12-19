package ttps.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ttps.spring.dto.BarrioDto;
import ttps.spring.entity.Barrio;
import ttps.spring.entity.Ciudad;
import ttps.spring.exception.barrio.BarrioNoEncontradoException;
import ttps.spring.model.ErrorResponse;
import ttps.spring.service.GenericService;
import ttps.spring.service.BarrioService;
import ttps.spring.service.CiudadService;

import java.util.List;
import java.util.stream.Collectors;

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

    // Endpoint específico para obtener todos los barrios como DTOs
    @GetMapping("/todos")
    public ResponseEntity<List<BarrioDto>> obtenerTodosLosBarrios() {
        List<Barrio> barrios = service.listar();
        List<BarrioDto> barriosDto = barrios.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());

        System.out.println("DEBUG - Listando todos los barrios: " + barriosDto.size() + " barrios encontrados");
        if (barriosDto.size() > 0) {
            System.out.println("DEBUG - Primer barrio: id=" + barriosDto.get(0).getId() + ", nombre=" + barriosDto.get(0).getNombre());
        }

        return ResponseEntity.ok(barriosDto);
    }

    @GetMapping("/ciudad/{ciudadId}")
    public ResponseEntity<List<BarrioDto>> getBarriosByCiudad(@PathVariable Long ciudadId) {
        Ciudad ciudad = ciudadService.obtener(ciudadId).orElse(null);
        if (ciudad == null) {
            return ResponseEntity.notFound().build();
        }
        List<Barrio> barrios = service.findByCiudad(ciudad);
        List<BarrioDto> barriosDto = barrios.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(barriosDto);
    }

    // Método auxiliar para convertir Barrio a BarrioDto
    private BarrioDto convertirADto(Barrio barrio) {
        BarrioDto dto = new BarrioDto();
        dto.setId(barrio.getId());
        dto.setNombre(barrio.getNombre());

        if (barrio.getCiudad() != null) {
            dto.setCiudadId(barrio.getCiudad().getId());
            dto.setCiudadNombre(barrio.getCiudad().getNombre());
        }

        return dto;
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
