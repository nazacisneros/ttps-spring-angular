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
        System.out.println("DEBUG Service - eliminarMascota llamado:");
        System.out.println("  usuarioId: " + usuarioId);
        System.out.println("  mascotaId: " + mascotaId);

        Mascota mascota = repository.findById(mascotaId)
                .orElseThrow(() -> new EntityNotFoundException("Mascota no encontrada con ID: " + mascotaId));

        System.out.println("  Mascota encontrada: " + mascota.getNombre());
        System.out.println("  Mascota.getUsuario() es null?: " + (mascota.getUsuario() == null));

        if (mascota.getUsuario() == null) {
            System.out.println("  ERROR: La mascota no tiene usuario asignado!");
            throw new IllegalArgumentException("La mascota no tiene un usuario asignado. " +
                    "Posiblemente fue creada antes de implementar la asignación de usuarios.");
        }

        Long usuarioDuenioId = mascota.getUsuario().getId();
        System.out.println("  Usuario dueño ID: " + usuarioDuenioId);
        System.out.println("  Usuario solicitante ID: " + usuarioId);
        System.out.println("  IDs coinciden?: " + usuarioDuenioId.equals(usuarioId));

        if (!usuarioDuenioId.equals(usuarioId)) {
            System.out.println("  ERROR: Los IDs no coinciden!");
            throw new IllegalArgumentException("No tienes permiso para eliminar esta mascota. " +
                    "Dueño: " + usuarioDuenioId + ", Solicitante: " + usuarioId);
        }

        System.out.println("  Validación OK - Procediendo a eliminar mascota");
        repository.deleteById(mascotaId);
        System.out.println("  Mascota eliminada exitosamente");
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