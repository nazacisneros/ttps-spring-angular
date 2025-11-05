package ttps.spring.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ttps.spring.entity.Ranking;

import java.util.List;

@Repository
@Transactional
public class RankingRepository {

    @PersistenceContext
    private EntityManager em;

    public void guardar(Ranking ranking) {
        em.persist(ranking);
    }

    public Ranking buscarPorId(Long id) {
        return em.find(Ranking.class, id);
    }

    public List<Ranking> listar() {
        return em.createQuery("SELECT r FROM Ranking r", Ranking.class).getResultList();
    }

    public void eliminar(Long id) {
        Ranking r = em.find(Ranking.class, id);
        if (r != null) {
            em.remove(r);
        }
    }

    public void actualizar(Ranking ranking) {
        em.merge(ranking);
    }
}

