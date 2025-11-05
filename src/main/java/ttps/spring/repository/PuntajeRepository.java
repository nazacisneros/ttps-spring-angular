package ttps.spring.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ttps.spring.entity.Puntaje;

import java.util.List;

@Repository
@Transactional
public class PuntajeRepository {

    @PersistenceContext
    private EntityManager em;

    public void guardar(Puntaje puntaje) {
        em.persist(puntaje);
    }

    public Puntaje buscarPorId(Long id) {
        return em.find(Puntaje.class, id);
    }

    public List<Puntaje> listar() {
        return em.createQuery("SELECT p FROM Puntaje p", Puntaje.class).getResultList();
    }

    public void eliminar(Long id) {
        Puntaje p = em.find(Puntaje.class, id);
        if (p != null) {
            em.remove(p);
        }
    }

    public void actualizar(Puntaje puntaje) {
        em.merge(puntaje);
    }
}

