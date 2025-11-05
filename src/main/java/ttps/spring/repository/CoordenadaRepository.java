package ttps.spring.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ttps.spring.entity.Coordenada;

@Repository
@Transactional
public class CoordenadaRepository {

    @PersistenceContext
    private EntityManager em;

    public void guardar(Coordenada coordenada) {
        em.persist(coordenada);
    }

    public Coordenada buscarPorId(Long id) {
        return em.find(Coordenada.class, id);
    }
}