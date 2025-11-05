package ttps.spring.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ttps.spring.entity.Publicacion;

import java.util.List;

@Repository
@Transactional
public class PublicacionRepository {

    @PersistenceContext
    private EntityManager em;

    public void guardar(Publicacion publicacion) {
        em.persist(publicacion);
    }

    public Publicacion buscarPorId(Long id) {
        return em.find(Publicacion.class, id);
    }

    public List<Publicacion> listar() {
        return em.createQuery("SELECT p FROM Publicacion p", Publicacion.class).getResultList();
    }

    public void eliminar(Long id) {
        Publicacion p = em.find(Publicacion.class, id);
        if (p != null) {
            em.remove(p);
        }
    }

    public void actualizar(Publicacion publicacion) {
        em.merge(publicacion);
    }
}

