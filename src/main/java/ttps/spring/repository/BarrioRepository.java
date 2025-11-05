package ttps.spring.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ttps.spring.entity.Barrio;

@Repository
@Transactional
public class BarrioRepository {

    @PersistenceContext
    private EntityManager em;

    public void guardar(Barrio barrio) {
        em.persist(barrio);
    }
}
