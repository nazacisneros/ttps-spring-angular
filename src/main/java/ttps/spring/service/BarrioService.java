package ttps.spring.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ttps.spring.entity.Barrio;
import ttps.spring.entity.Ciudad;
import ttps.spring.repository.BarrioRepository;
import java.util.List;

@Service
public class BarrioService extends GenericService<Barrio, Long> {

    private final BarrioRepository repository;

    public BarrioService(BarrioRepository repository) {
        this.repository = repository;
    }

    @Override
    protected JpaRepository<Barrio, Long> getRepository() {
        return repository;
    }

    @Override
    protected String getEntityName() {
        return "Barrio";
    }

    public List<Barrio> findByCiudad(Ciudad ciudad) {
        return repository.findByCiudad(ciudad);
    }
}
