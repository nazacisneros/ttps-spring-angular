package ttps.spring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ttps.spring.model.LoginRequest;
import ttps.spring.model.LoginResponse;
import ttps.spring.model.RegistroRequest;
import ttps.spring.entity.*;
import ttps.spring.service.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrar(@RequestBody RegistroRequest request) {
        Usuario usuario = usuarioService.registrar(request);
        return ResponseEntity.created(URI.create("/api/usuarios/" + usuario.getId())).body(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Usuario usuario = usuarioService.login(request);
        LoginResponse response = new LoginResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.isEsAdmin()
        );
        return ResponseEntity.ok(response);
    }
}
