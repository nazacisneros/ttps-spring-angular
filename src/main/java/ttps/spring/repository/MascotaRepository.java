
package ttps.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ttps.spring.entity.Mascota;
import java.util.List;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {
    List<Mascota> findByUsuarioId(Long usuarioId);
    List<Mascota> findByEstado(String estado);
    List<Mascota> findByUsuarioIdAndEstado(Long usuarioId, String estado);
}
