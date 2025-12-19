package ttps.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ttps.spring.entity.Barrio;
import ttps.spring.entity.Ciudad;

import java.util.List;
import java.util.Optional;

@Repository
public interface BarrioRepository extends JpaRepository<Barrio, Long> {

    List<Barrio> findByCiudad(Ciudad ciudad);

    Optional<Barrio> findByNombreAndCiudad(String nombre, Ciudad ciudad);
}
