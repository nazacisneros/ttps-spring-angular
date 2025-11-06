package ttps.spring.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ttps.spring.entity.Puntaje;
import ttps.spring.repository.PuntajeRepository;

@Service
public class PuntajeService extends GenericService<Puntaje, Long> {

    private final PuntajeRepository repository;

    public PuntajeService(PuntajeRepository repository) {
        this.repository = repository;
    }

    @Override
    protected JpaRepository<Puntaje, Long> getRepository() {
        return repository;
    }

    @Override
    protected String getEntityName() {
        return "Puntaje";
    }
}