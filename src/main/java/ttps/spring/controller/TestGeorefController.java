package ttps.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ttps.spring.dto.UbicacionResponse;
import ttps.spring.model.ErrorResponse;
import ttps.spring.service.GeorefService;

@RestController
@RequestMapping("/api/test")
public class TestGeorefController {

    private final GeorefService georefService;

    public TestGeorefController(GeorefService georefService) {
        this.georefService = georefService;
    }

    /**
     * Endpoint de prueba para verificar la integración con Georef API
     * Ejemplo: GET /api/test/georef?lat=-34.6037&lon=-58.3816
     */
    @GetMapping("/georef")
    public ResponseEntity<UbicacionResponse> probarGeoref(
            @RequestParam Double lat,
            @RequestParam Double lon
    ) {
        System.out.println("DEBUG - Test Georef llamado con lat=" + lat + ", lon=" + lon);
        UbicacionResponse ubicacion = georefService.obtenerUbicacion(lat, lon);

        if (ubicacion == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ubicacion);
    }

    // Exception Handlers
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Parámetros inválidos: " + ex.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error al consultar Georef API: " + ex.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

