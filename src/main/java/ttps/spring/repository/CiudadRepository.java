package ttps.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ttps.spring.entity.Ciudad;

import java.util.Optional;

@Repository
public interface CiudadRepository extends JpaRepository<Ciudad, Long> {

    /**
     * Busca una ciudad por su nombre exacto
     * @param nombre Nombre de la ciudad
     * @return Optional con la ciudad si existe
     */
    Optional<Ciudad> findByNombre(String nombre);
}
