//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ttps.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ttps.spring.entity.Publicacion;
import java.time.LocalDate;
import java.util.List;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {
    List<Publicacion> findByUsuarioPublicadorId(Long usuarioId);
    List<Publicacion> findByEstado(String estado);
    List<Publicacion> findByFechaAfter(LocalDate fecha);
}
