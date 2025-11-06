package ttps.spring.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ttps.spring.entity.Medalla;
import ttps.spring.repository.MedallaRepository;

@Service
public class MedallaService extends GenericService<Medalla, Long> {

    private final MedallaRepository repository;

    public MedallaService(MedallaRepository repository) {
        this.repository = repository;
    }

    @Override
    protected JpaRepository<Medalla, Long> getRepository() {
        return repository;
    }

    @Override
    protected String getEntityName() {
        return "Medalla";
    }
}