package ttps.spring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ttps.spring.model.LoginRequest;
import ttps.spring.model.LoginResponse;
import ttps.spring.model.RegistroRequest;
import ttps.spring.model.UsuarioResponse;
import ttps.spring.security.JwtUtil;
import ttps.spring.entity.*;
import ttps.spring.service.*;
import org.springframework.security.core.Authentication;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
class AuthController {

    private final UsuarioService usuarioService;

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioService usuarioService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrar(@RequestBody RegistroRequest request) {
        Usuario usuario = usuarioService.registrar(request);
        return ResponseEntity.created(URI.create("/api/usuarios/" + usuario.getId())).body(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getContrasenia()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuario = usuarioService.findByEmail(userDetails.getUsername());

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
        Usuario usuario = usuarioService.findByEmail(email);
        return new UsuarioResponse(usuario);
    }
}
