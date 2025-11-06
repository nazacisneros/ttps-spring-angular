package ttps.spring.controller;

import org.springframework.web.bind.annotation.*;
import ttps.spring.entity.Coordenada;
import ttps.spring.service.GenericService;
import ttps.spring.service.CoordenadaService;

@RestController
@RequestMapping("/api/coordenadaes")
public class CoordenadaController extends GenericController<Coordenada, Long> {

    private final CoordenadaService service;

    public CoordenadaController(CoordenadaService service) {
        this.service = service;
    }

    @Override
    protected GenericService<Coordenada, Long> getService() {
        return service;
    }

    @Override
    protected String getBasePath() {
        return "/api/coordenadaes";
    }

    @Override
    protected Long getId(Coordenada entidad) {
        return entidad.getId();
    }

}