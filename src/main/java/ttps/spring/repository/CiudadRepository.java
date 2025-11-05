package ttps.spring.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ttps.spring.entity.Ciudad;

@Repository
@Transactional
public class CiudadRepository {

    @PersistenceContext
    private EntityManager em;

    public void guardar(Ciudad ciudad) {
        em.persist(ciudad);
    }

    public Ciudad buscarPorId(Long id) {
        return em.find(Ciudad.class, id);
    }

    public void eliminar(Ciudad ciudad) {
        em.remove(em.contains(ciudad) ? ciudad : em.merge(ciudad));
    }
}

