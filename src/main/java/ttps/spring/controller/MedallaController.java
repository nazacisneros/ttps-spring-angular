package ttps.spring.controller;

import org.springframework.web.bind.annotation.*;
import ttps.spring.entity.Medalla;
import ttps.spring.service.GenericService;
import ttps.spring.service.MedallaService;

@RestController
@RequestMapping("/api/medallas")
public class MedallaController extends GenericController<Medalla, Long> {

    private final MedallaService service;

    public MedallaController(MedallaService service) {
        this.service = service;
    }

    @Override
    protected GenericService<Medalla, Long> getService() {
        return service;
    }

    @Override
    protected String getBasePath() {
        return "/api/medallas";
    }

    @Override
    protected Long getId(Medalla entidad) {
        return entidad.getId();
    }

}