package ttps.spring.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import ttps.spring.entity.Avistamiento;

import java.util.List;

@Repository
@Transactional
public class AvistamientoRepository {

    @PersistenceContext
    private EntityManager em;

    public void guardar(Avistamiento avistamiento) {
        em.persist(avistamiento);
    }

    public List<Avistamiento> listar() {
        return em.createQuery("FROM Avistamiento", Avistamiento.class).getResultList();
    }

    public Avistamiento buscarPorId(Long id) {
        return em.find(Avistamiento.class, id);
    }

    public void eliminar(Long id) {
        Avistamiento a = buscarPorId(id);
        if (a != null) {
            em.remove(a);
        }
    }
}
