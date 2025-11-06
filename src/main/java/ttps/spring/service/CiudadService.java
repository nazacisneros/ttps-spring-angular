package ttps.spring.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ttps.spring.entity.Ciudad;
import ttps.spring.repository.CiudadRepository;

@Service
public class CiudadService extends GenericService<Ciudad, Long> {

    private final CiudadRepository repository;

    public CiudadService(CiudadRepository repository) {
        this.repository = repository;
    }

    @Override
    protected JpaRepository<Ciudad, Long> getRepository() {
        return repository;
    }

    @Override
    protected String getEntityName() {
        return "Ciudad";
    }
}