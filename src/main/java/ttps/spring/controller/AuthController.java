package ttps.spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ttps.spring.exception.auth.*;
import ttps.spring.model.LoginRequest;
import ttps.spring.model.LoginResponse;
import ttps.spring.model.RegistroRequest;
import ttps.spring.model.ErrorResponse;
import ttps.spring.entity.Usuario;
import ttps.spring.service.UsuarioService;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro")
    public ResponseEntity<LoginResponse> registrar(@RequestBody RegistroRequest request) {
        logger.info("Intento de registro para email: {}", request.getEmail());
        try {
            validarRegistroRequest(request);
            Usuario usuario = usuarioService.registrar(request);
            logger.info("Usuario registrado exitosamente: {}", usuario.getId());
            LoginResponse response = new LoginResponse(
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getApellido(),
                    usuario.getEmail(),
                    usuario.isEsAdmin()
            );
            return ResponseEntity.created(URI.create("/api/auth/usuarios/" + usuario.getId())).body(response);
        } catch (UsuarioYaExisteException e) {
            logger.warn("Intento de registro con email duplicado: {}", request.getEmail());
            throw e;
        } catch (ValidationException e) {
            logger.warn("Validación fallida en registro: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error inesperado durante el registro", e);
            throw new InternalServerException("Error al registrar usuario");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        logger.info("Intento de login para email: {}", request.getEmail());
        try {
            validarLoginRequest(request);
            Usuario usuario = usuarioService.login(request);
            logger.info("Login exitoso para usuario: {}", usuario.getId());
            LoginResponse response = new LoginResponse(
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getApellido(),
                    usuario.getEmail(),
                    usuario.isEsAdmin()
            );
            return ResponseEntity.ok(response);
        } catch (UsuarioNoEncontradoException e) {
            logger.warn("Intento de login con usuario no encontrado: {}", request.getEmail());
            throw e;
        } catch (CredencialesInvalidasException e) {
            logger.warn("Intento de login con credenciales inválidas para: {}", request.getEmail());
            throw e;
        } catch (UsuarioDeshabilitadoException e) {
            logger.warn("Intento de login con usuario deshabilitado: {}", request.getEmail());
            throw e;
        } catch (ValidationException e) {
            logger.warn("Validación fallida en login: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error inesperado durante el login", e);
            throw new InternalServerException("Error al procesar login");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        logger.info("Logout requerido");
        if (token == null || token.isEmpty()) {
            logger.warn("Intento de logout sin token");
            throw new ValidationException("Token no proporcionado");
        }
        try {
            // Aquí puedes invalidar el token si usas JWT
            logger.info("Logout exitoso");
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error durante logout", e);
            throw new InternalServerException("Error al procesar logout");
        }
    }

    private void validarRegistroRequest(RegistroRequest request) {
        if (request == null) {
            throw new ValidationException("Datos de registro no pueden ser nulos");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new ValidationException("El email es requerido");
        }
        if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("El formato del email no es válido");
        }
        if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
            throw new ValidationException("El nombre es requerido");
        }
        if (request.getApellido() == null || request.getApellido().trim().isEmpty()) {
            throw new ValidationException("El apellido es requerido");
        }
        if (request.getContrasenia() == null || request.getContrasenia().isEmpty()) {
            throw new ValidationException("La contraseña es requerida");
        }
        if (request.getContrasenia().length() < 8) {
            throw new ValidationException("La contraseña debe tener al menos 8 caracteres");
        }
        if (!request.getContrasenia().matches(".*[A-Z].*")) {
            throw new ValidationException("La contraseña debe contener al menos una mayúscula");
        }
        if (!request.getContrasenia().matches(".*[a-z].*")) {
            throw new ValidationException("La contraseña debe contener al menos una minúscula");
        }
        if (!request.getContrasenia().matches(".*\\d.*")) {
            throw new ValidationException("La contraseña debe contener al menos un número");
        }
    }

    private void validarLoginRequest(LoginRequest request) {
        if (request == null) {
            throw new ValidationException("Datos de login no pueden ser nulos");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new ValidationException("El email es requerido");
        }
        if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("El formato del email no es válido");
        }
        if (request.getContrasenia() == null || request.getContrasenia().isEmpty()) {
            throw new ValidationException("La contraseña es requerida");
        }
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException e) {
        logger.error("Excepción de validación: {}", e.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(UsuarioYaExisteException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioYaExiste(UsuarioYaExisteException e) {
        logger.error("Usuario ya existe: {}", e.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                e.getMessage(),
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioNoEncontrado(UsuarioNoEncontradoException e) {
        logger.error("Usuario no encontrado: {}", e.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<ErrorResponse> handleCredencialesInvalidas(CredencialesInvalidasException e) {
        logger.error("Credenciales inválidas");
        ErrorResponse error = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                e.getMessage(),
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(UsuarioDeshabilitadoException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioDeshabilitado(UsuarioDeshabilitadoException e) {
        logger.error("Usuario deshabilitado: {}", e.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                e.getMessage(),
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerException(InternalServerException e) {
        logger.error("Error interno del servidor: {}", e.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getMessage(),
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        logger.error("Error no controlado", e);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ha ocurrido un error inesperado",
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}