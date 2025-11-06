package ttps.spring.controller;

import org.springframework.web.bind.annotation.*;
import ttps.spring.entity.Barrio;
import ttps.spring.service.GenericService;
import ttps.spring.service.BarrioService;

@RestController
@RequestMapping("/api/barrios")
public class BarrioController extends GenericController<Barrio, Long> {

    private final BarrioService service;

    public BarrioController(BarrioService service) {
        this.service = service;
    }

    @Override
    protected GenericService<Barrio, Long> getService() {
        return service;
    }

    @Override
    protected String getBasePath() {
        return "/api/barrios";
    }

    @Override
    protected Long getId(Barrio entidad) {
        return entidad.getId();
    }

}