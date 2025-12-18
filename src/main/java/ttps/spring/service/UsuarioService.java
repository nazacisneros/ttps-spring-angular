package ttps.spring.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ttps.spring.entity.Barrio;
import ttps.spring.entity.Ranking;
import ttps.spring.entity.Usuario;
import ttps.spring.repository.UsuarioRepository;
import ttps.spring.model.LoginRequest;
import ttps.spring.model.RegistroRequest;
import ttps.spring.service.BarrioService;
import ttps.spring.service.RankingService;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UsuarioService extends GenericService<Usuario, Long> {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final BarrioService barrioService;
    private final RankingService rankingService;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder,
            BarrioService barrioService, RankingService rankingService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.barrioService = barrioService;
        this.rankingService = rankingService;
    }

    @Override
    protected JpaRepository<Usuario, Long> getRepository() {
        return repository;
    }

    @Override
    protected String getEntityName() {
        return "Usuario";
    }

    public Usuario registrar(RegistroRequest request) {

        if (buscarPorEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException(
                    "Ya existe una cuenta con este correo electrónico. Por favor, utiliza otro email o intenta iniciar sesión.");
        }

        Ranking ranking = new Ranking("Principiante", 0);
        rankingService.crear(ranking);

        Barrio barrio = null;
        if (request.getBarrioId() != null) {
            barrio = barrioService.obtener(request.getBarrioId())
                    .orElseThrow(() -> new IllegalArgumentException("Barrio no encontrado"));
        }

        Usuario usuario = new Usuario(
                request.getNombre(),
                request.getApellido(),
                request.getEmail(),
                request.getTelefono(),
                passwordEncoder.encode(request.getContrasenia()),
                true,
                false,
                barrio,
                ranking);

        return repository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario login(LoginRequest request) {
        Usuario usuario = buscarPorEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Email o contraseña incorrectos"));

        if (!passwordEncoder.matches(
                request.getContrasenia(),
                usuario.getContrasenia())) {
            throw new IllegalArgumentException("Email o contraseña incorrectos");
        }

        if (!usuario.isCondicion()) {
            throw new IllegalStateException("Usuario deshabilitado");
        }

        return usuario;
    }

    public Usuario editarPerfil(Long id, Usuario datosActualizados) {
        Usuario existente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + id));

        if (datosActualizados.getNombre() != null) {
            existente.setNombre(datosActualizados.getNombre());
        }
        if (datosActualizados.getApellido() != null) {
            existente.setApellido(datosActualizados.getApellido());
        }
        if (datosActualizados.getTelefono() != null) {
            existente.setTelefono(datosActualizados.getTelefono());
        }
        if (datosActualizados.getBarrio() != null) {
            existente.setBarrio(datosActualizados.getBarrio());
        }

        return repository.save(existente);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        return repository.findByEmail(email);
    }

    public void deshabilitarUsuario(Long id) {
        Usuario usuario = obtener(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + id));

        usuario.deshabilitarCuenta();
        repository.save(usuario);
    }

    public void habilitarUsuario(Long id) {
        Usuario usuario = obtener(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + id));

        usuario.habilitarCuenta();
        repository.save(usuario);
    }

}
