package ttps.spring.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Transactional
public abstract class GenericService<T, ID> {

    protected abstract JpaRepository<T, ID> getRepository();
    protected abstract String getEntityName();

    @Transactional(readOnly = true)
    public List<T> listar() {
        return getRepository().findAll();
    }

    @Transactional(readOnly = true)
    public Optional<T> obtener(ID id) {
        return getRepository().findById(id);
    }

    public T crear(T entidad) {
        return getRepository().save(entidad);
    }

    public T actualizar(ID id, T entidad) {
        if (!getRepository().existsById(id)) {
            throw new EntityNotFoundException("Entidad no encontrada: " + id);
        }
        return getRepository().save(entidad);
    }

    public void eliminar(ID id) {
        if (!getRepository().existsById(id)) {
            throw new EntityNotFoundException("Entidad no encontrada: " + id);
        }
        getRepository().deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existe(ID id) {
        return getRepository().existsById(id);
    }

    @Transactional(readOnly = true)
    public long contar() {
        return getRepository().count();
    }
}