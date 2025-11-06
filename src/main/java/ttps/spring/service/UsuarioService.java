package ttps.spring.service;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ttps.spring.entity.Usuario;
import ttps.spring.repository.UsuarioRepository;
import ttps.spring.model.LoginRequest;
import ttps.spring.model.RegistroRequest;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UsuarioService extends GenericService<Usuario, Long> {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
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
        // Validar que el email no exista
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());
        // usuario.setContrasenia(passwordEncoder.encode(request.getContrasenia())); // Con seguridad
        usuario.setContrasenia(request.getContrasenia()); // Sin seguridad (temporal)
        usuario.setCondicion(true);
        usuario.setEsAdmin(false);

        return repository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario login(LoginRequest request) {
        Usuario usuario = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Email o contraseña incorrectos"));

        // Sin seguridad (temporal)
        if (!usuario.getContrasenia().equals(request.getContrasenia())) {
            throw new IllegalArgumentException("Email o contraseña incorrectos");
        }

        // Con seguridad:
        // if (!passwordEncoder.matches(request.getContrasenia(), usuario.getContrasenia())) {
        //     throw new IllegalArgumentException("Email o contraseña incorrectos");
        // }

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