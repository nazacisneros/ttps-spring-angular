package ttps.spring.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ttps.spring.entity.Avistamiento;
import ttps.spring.entity.Mascota;
import ttps.spring.entity.Usuario;
import ttps.spring.repository.AvistamientoRepository;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AvistamientoService extends GenericService<Avistamiento, Long> {

    private final AvistamientoRepository repository;
    private final UsuarioService usuarioService;
    private final MascotaService mascotaService;

    public AvistamientoService(
            AvistamientoRepository repository,
            UsuarioService usuarioService,
            MascotaService mascotaService) {
        this.repository = repository;
        this.usuarioService = usuarioService;
        this.mascotaService = mascotaService;
    }

    @Override
    protected JpaRepository<Avistamiento, Long> getRepository() {
        return repository;
    }

    @Override
    protected String getEntityName() {
        return "Avistamiento";
    }

    public Avistamiento crearAvistamiento(Long usuarioId, Long mascotaId, Avistamiento avistamiento) {
        Usuario usuario = usuarioService.obtener(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Mascota mascota = mascotaService.obtener(mascotaId)
                .orElseThrow(() -> new EntityNotFoundException("Mascota no encontrada"));

        avistamiento.setUsuario_avistador(usuario);
        avistamiento.setMascota(mascota);
        avistamiento.setFecha(LocalDate.now());
        avistamiento.setHora(LocalDateTime.now());

        usuario.registrarAvistamiento(avistamiento);

        return repository.save(avistamiento);
    }

    @Transactional(readOnly = true)
    public List<Avistamiento> listarPorMascota(Long mascotaId) {
        if (!mascotaService.existe(mascotaId)) {
            throw new EntityNotFoundException("Mascota no encontrada con id: " + mascotaId);
        }
        return repository.findByMascotaId(mascotaId);
    }

    @Transactional(readOnly = true)
    public List<Avistamiento> listarPorUsuario(Long usuarioId) {
        if (!usuarioService.existe(usuarioId)) {
            throw new EntityNotFoundException("Usuario no encontrado con id: " + usuarioId);
        }
        return repository.findByUsuarioAvistadorId(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<Avistamiento> listarRecientes() {
        LocalDate hace7Dias = LocalDate.now().minusDays(7);
        return repository.findByFechaAfter(hace7Dias);
    }

    /*
     * @Transactional(readOnly = true)
     * public List<Avistamiento> listarPorZona() {
     * return null;
     * }
     * 
     */

}