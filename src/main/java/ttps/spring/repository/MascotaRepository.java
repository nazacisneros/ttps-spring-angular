package ttps.spring.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import ttps.spring.entity.Mascota;

import java.util.List;

@Repository
@Transactional
public class MascotaRepository {

    @PersistenceContext
    private EntityManager em;

    public void guardar(Mascota mascota) {
        em.persist(mascota);
    }

    public List<Mascota> listar() {
        return em.createQuery("FROM Mascota", Mascota.class).getResultList();
    }

    public Mascota buscarPorId(Long id) {
        return em.find(Mascota.class, id);
    }

    public void eliminar(Long id) {
        Mascota m = buscarPorId(id);
        if (m != null) {
            em.remove(m);
        }
    }
}