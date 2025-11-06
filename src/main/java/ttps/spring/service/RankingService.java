package ttps.spring.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ttps.spring.entity.Ranking;
import ttps.spring.repository.RankingRepository;

@Service
public class RankingService extends GenericService<Ranking, Long> {

    private final RankingRepository repository;

    public RankingService(RankingRepository repository) {
        this.repository = repository;
    }

    @Override
    protected JpaRepository<Ranking, Long> getRepository() {
        return repository;
    }

    @Override
    protected String getEntityName() {
        return "Ranking";
    }

}