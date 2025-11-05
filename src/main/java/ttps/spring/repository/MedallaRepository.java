package ttps.spring.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import ttps.spring.entity.Medalla;

import java.util.List;

@Repository
@Transactional
public class MedallaRepository {

    @PersistenceContext
    private EntityManager em;

    public void guardar(Medalla medalla) {
        em.persist(medalla);
    }

    public List<Medalla> listar() {
        return em.createQuery("FROM Medalla", Medalla.class).getResultList();
    }

    public Medalla buscarPorId(Long id) {
        return em.find(Medalla.class, id);
    }

    public void eliminar(Long id) {
        Medalla m = buscarPorId(id);
        if (m != null) {
            em.remove(m);
        }
    }
}

