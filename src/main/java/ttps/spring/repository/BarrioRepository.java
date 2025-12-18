package ttps.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ttps.spring.entity.Barrio;
import ttps.spring.entity.Ciudad;
import java.util.List;

public interface BarrioRepository extends JpaRepository<Barrio, Long> {
    List<Barrio> findByCiudad(Ciudad ciudad);
}
