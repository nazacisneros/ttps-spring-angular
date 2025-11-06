package ttps.spring.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ttps.spring.entity.Mascota;
import ttps.spring.entity.Usuario;
import ttps.spring.repository.MascotaRepository;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class MascotaService extends GenericService<Mascota, Long> {

    private final MascotaRepository repository;
    private final UsuarioService usuarioService;

    public MascotaService(MascotaRepository repository, UsuarioService usuarioService) {
        this.repository = repository;
        this.usuarioService = usuarioService;
    }

    @Override
    protected JpaRepository<Mascota, Long> getRepository() {
        return repository;
    }

    @Override
    protected String getEntityName() {
        return "Mascota";
    }

    public Mascota crearMascota(Long usuarioId, Mascota mascota) {
        Usuario usuario = usuarioService.obtener(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        mascota.setUsuario(usuario);
        return repository.save(mascota);
    }

    @Transactional(readOnly = true)
    public List<Mascota> listarPorUsuario(Long usuarioId) {
        if (!usuarioService.existe(usuarioId)) {
            throw new EntityNotFoundException("Usuario no encontrado con id: " + usuarioId);
        }
        return repository.findByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<Mascota> listarPerdidas() {
        return repository.findByEstado("PERDIDA");
    }

    public Mascota editarMascota(Long usuarioId, Long mascotaId, Mascota datosActualizados) {
        Mascota mascota = obtener(mascotaId)
                .orElseThrow(() -> new EntityNotFoundException("Mascota no encontrado"));

        if (!mascota.getUsuario().getId().equals(usuarioId)) {
            throw new IllegalArgumentException("No tienes permiso para editar esta mascota");
        }

        if (datosActualizados.getNombre() != null) {
            mascota.setNombre(datosActualizados.getNombre());
        }
        if (datosActualizados.getDescripcion() != null) {
            mascota.setDescripcion(datosActualizados.getDescripcion());
        }
        if (datosActualizados.getEstado() != null) {
            mascota.setEstado(datosActualizados.getEstado());
        }
        if (datosActualizados.getFotos() != null) {
            mascota.setFotos(datosActualizados.getFotos());
        }

        return repository.save(mascota);
    }

    public void eliminarMascota(Long usuarioId, Long mascotaId) {
        Mascota mascota = obtener(mascotaId)
                .orElseThrow(() -> new EntityNotFoundException("Mascota no encontrado"));

        if (!mascota.getUsuario().getId().equals(usuarioId)) {
            throw new IllegalArgumentException("No tienes permiso para eliminar esta mascota");
        }

        repository.deleteById(mascotaId);
    }

    public Mascota marcarComoPerdida(Long mascotaId) {
        Mascota mascota = obtener(mascotaId)
                .orElseThrow(() -> new EntityNotFoundException("Mascota no encontrado"));
        mascota.setEstado("PERDIDA");
        return repository.save(mascota);
    }

    public Mascota marcarComoEncontrada(Long mascotaId) {
        Mascota mascota = obtener(mascotaId)
                .orElseThrow(() -> new EntityNotFoundException("Mascota no encontrado"));
        mascota.setEstado("ENCONTRADA");
        return repository.save(mascota);
    }
}