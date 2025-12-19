package ttps.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ttps.spring.exception.auth.*;
import ttps.spring.model.ErrorResponse;
import ttps.spring.model.LoginRequest;
import ttps.spring.model.LoginResponse;
import ttps.spring.model.RegistroRequest;
import ttps.spring.model.UsuarioResponse;
import ttps.spring.model.UsuarioUpdateRequest;
import ttps.spring.security.JwtUtil;
import ttps.spring.entity.*;
import ttps.spring.service.*;
import org.springframework.security.core.Authentication;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
class AuthController {

    private final UsuarioService usuarioService;
    private final BarrioService barrioService;

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioService usuarioService, BarrioService barrioService,
            AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.barrioService = barrioService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody RegistroRequest request) {
        try {
            Usuario usuario = usuarioService.registrar(request);
            return ResponseEntity.created(URI.create("/api/usuarios/" + usuario.getId())).body(usuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Error interno del servidor"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getContrasenia()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuario = usuarioService.buscarPorEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new LoginResponse(usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.isEsAdmin(),
                token));
    }

    @GetMapping("/me")
    public UsuarioResponse me(Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return new UsuarioResponse(usuario);
    }

    @PutMapping("/me")
    public ResponseEntity<UsuarioResponse> editarMiPerfil(@RequestBody UsuarioUpdateRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        Usuario existente = usuarioService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (request.getNombre() != null) {
            existente.setNombre(request.getNombre());
        }
        if (request.getApellido() != null) {
            existente.setApellido(request.getApellido());
        }
        if (request.getTelefono() != null) {
            existente.setTelefono(request.getTelefono());
        }

        if (request.getBarrioId() != null) {
            Barrio barrio = barrioService.obtener(request.getBarrioId()).orElse(null);
            existente.setBarrio(barrio);
        }

        Usuario actualizado = usuarioService.actualizar(existente.getId(), existente);
        return ResponseEntity.ok(new UsuarioResponse(actualizado));
    }

    // Exception Handlers
    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<ErrorResponse> handleCredencialesInvalidasException(CredencialesInvalidasException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsuarioYaExisteException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioYaExisteException(UsuarioYaExisteException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsuarioDeshabilitadoException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioDeshabilitadoException(UsuarioDeshabilitadoException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Credenciales incorrectas"));
    }
}
