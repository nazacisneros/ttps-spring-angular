package ttps.spring.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ttps.spring.entity.Publicacion;
import ttps.spring.repository.PublicacionRepository;

@Service
public class PublicacionService extends GenericService<Publicacion, Long> {

    private final PublicacionRepository repository;

    public PublicacionService(PublicacionRepository repository) {
        this.repository = repository;
    }

    @Override
    protected JpaRepository<Publicacion, Long> getRepository() {
        return repository;
    }

    @Override
    protected String getEntityName() {
        return "Publicacion";
    }
}