package ttps.spring.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ttps.spring.dto.UbicacionResponse;
import ttps.spring.entity.Barrio;
import ttps.spring.entity.Ciudad;
import ttps.spring.entity.Ranking;
import ttps.spring.entity.Usuario;
import ttps.spring.repository.BarrioRepository;
import ttps.spring.repository.CiudadRepository;
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
    private final GeorefService georefService;
    private final CiudadRepository ciudadRepository;
    private final BarrioRepository barrioRepository;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder,
            BarrioService barrioService, RankingService rankingService,
            GeorefService georefService, CiudadRepository ciudadRepository,
            BarrioRepository barrioRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.barrioService = barrioService;
        this.rankingService = rankingService;
        this.georefService = georefService;
        this.ciudadRepository = ciudadRepository;
        this.barrioRepository = barrioRepository;
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

        // 🆕 Prioridad 1: Si hay coordenadas, usar Georef para obtener ciudad/barrio
        if (request.getLatitud() != null && request.getLongitud() != null) {
            System.out.println("DEBUG - Registro con coordenadas - usando Georef");
            System.out.println("  Coordenadas: lat=" + request.getLatitud() + ", lng=" + request.getLongitud());

            UbicacionResponse ubicacion = georefService.obtenerUbicacion(
                    request.getLatitud(),
                    request.getLongitud()
            );

            if (ubicacion != null && ubicacion.getCiudad() != null) {
                // Buscar o crear Ciudad
                Ciudad ciudad = buscarOCrearCiudad(ubicacion.getCiudad());
                System.out.println("DEBUG - Ciudad asignada: " + ciudad.getNombre());

                // Buscar o crear Barrio
                if (ubicacion.getBarrio() != null) {
                    barrio = buscarOCrearBarrio(ubicacion.getBarrio(), ciudad);
                    System.out.println("DEBUG - Barrio asignado: " + barrio.getNombre());
                }
            } else {
                System.out.println("WARNING - No se pudo obtener ubicación desde Georef");
            }
        }
        // Prioridad 2: Si no hay coordenadas pero hay barrioId, usar el barrio especificado
        else if (request.getBarrioId() != null) {
            System.out.println("DEBUG - Registro con barrioId (modo legacy)");
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

    // Método auxiliar para buscar o crear Ciudad
    private Ciudad buscarOCrearCiudad(String nombreCiudad) {
        return ciudadRepository.findByNombre(nombreCiudad)
                .orElseGet(() -> {
                    System.out.println("DEBUG - Creando nueva ciudad: " + nombreCiudad);
                    Ciudad ciudad = new Ciudad();
                    ciudad.setNombre(nombreCiudad);
                    return ciudadRepository.save(ciudad);
                });
    }

    // Método auxiliar para buscar o crear Barrio
    private Barrio buscarOCrearBarrio(String nombreBarrio, Ciudad ciudad) {
        return barrioRepository.findByNombreAndCiudad(nombreBarrio, ciudad)
                .orElseGet(() -> {
                    System.out.println("DEBUG - Creando nuevo barrio: " + nombreBarrio);
                    Barrio barrio = new Barrio();
                    barrio.setNombre(nombreBarrio);
                    barrio.setCiudad(ciudad);
                    return barrioRepository.save(barrio);
                });
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
