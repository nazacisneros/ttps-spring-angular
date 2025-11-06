package ttps.spring.controller;

import org.springframework.web.bind.annotation.*;
import ttps.spring.entity.Puntaje;
import ttps.spring.service.GenericService;
import ttps.spring.service.PuntajeService;

@RestController
@RequestMapping("/api/puntajes")
public class PuntajeController extends GenericController<Puntaje, Long> {

    private final PuntajeService service;

    public PuntajeController(PuntajeService service) {
        this.service = service;
    }

    @Override
    protected GenericService<Puntaje, Long> getService() {
        return service;
    }

    @Override
    protected String getBasePath() {
        return "/api/puntajes";
    }

    @Override
    protected Long getId(Puntaje entidad) {
        return entidad.getId();
    }

}