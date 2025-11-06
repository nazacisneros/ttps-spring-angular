package ttps.spring.controller;

import org.springframework.web.bind.annotation.*;
import ttps.spring.entity.Ranking;
import ttps.spring.service.GenericService;
import ttps.spring.service.RankingService;

@RestController
@RequestMapping("/api/rankings")
public class RankingController extends GenericController<Ranking, Long> {

    private final RankingService service;

    public RankingController(RankingService service) {
        this.service = service;
    }

    @Override
    protected GenericService<Ranking, Long> getService() {
        return service;
    }

    @Override
    protected String getBasePath() {
        return "/api/rankings";
    }

    @Override
    protected Long getId(Ranking entidad) {
        return entidad.getId();
    }

}