package ttps.spring.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ttps.spring.entity.Coordenada;
import ttps.spring.repository.CoordenadaRepository;

@Service
public class CoordenadaService extends GenericService<Coordenada, Long> {

    private final CoordenadaRepository repository;

    public CoordenadaService(CoordenadaRepository repository) {
        this.repository = repository;
    }

    @Override
    protected JpaRepository<Coordenada, Long> getRepository() {
        return repository;
    }

    @Override
    protected String getEntityName() {
        return "Coordenada";
    }
}