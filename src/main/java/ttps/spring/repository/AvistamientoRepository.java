//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ttps.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ttps.spring.entity.Avistamiento;
import java.util.List;
import java.time.LocalDate;

public interface AvistamientoRepository extends JpaRepository<Avistamiento, Long> {
    List<Avistamiento> findByMascotaId(Long mascotaId);
    List<Avistamiento> findByUsuarioAvistadorId(Long usuarioId);
    List<Avistamiento> findByFechaAfter(LocalDate fecha);
    //List<Avistamiento> findByCoordenada_Zona();
    List<Avistamiento> findByMascotaIdOrderByFechaDesc(Long mascotaId);
}
