package ttps.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ttps.spring.dto.UbicacionResponse;
import ttps.spring.model.ErrorResponse;
import ttps.spring.model.RegistroRequest;
import ttps.spring.service.GeorefService;
import ttps.spring.service.UsuarioService;

@RestController
@RequestMapping("/api/test")
public class TestGeorefController {

    private final GeorefService georefService;
    private final UsuarioService usuarioService;

    public TestGeorefController(GeorefService georefService, UsuarioService usuarioService) {
        this.georefService = georefService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/georef")
    public ResponseEntity<UbicacionResponse> probarGeoref(
            @RequestParam Double lat,
            @RequestParam Double lon) {
        UbicacionResponse ubicacion = georefService.obtenerUbicacion(lat, lon);

        if (ubicacion == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ubicacion);
    }

    @PostMapping("/registro-con-coordenadas")
    public ResponseEntity<String> probarRegistroConCoordenadas() {
        try {
            RegistroRequest request = new RegistroRequest();
            request.setNombre("Usuario Test");
            request.setApellido("Georef");
            request.setEmail("test" + System.currentTimeMillis() + "@example.com");
            request.setTelefono("123456789");
            request.setContrasenia("password123");
            request.setLatitud(-34.6037);
            request.setLongitud(-58.3816);
            var usuario = usuarioService.registrar(request);

            return ResponseEntity.ok("Usuario registrado exitosamente: " + usuario.getNombre() +
                    " - Ciudad: "
                    + (usuario.getBarrio() != null && usuario.getBarrio().getCiudad() != null
                            ? usuario.getBarrio().getCiudad().getNombre()
                            : "Sin ciudad"));

        } catch (Exception e) {
            System.err.println("ERROR - Error en registro de prueba: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

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
