package ttps.spring.controller;

import org.springframework.web.bind.annotation.*;
import ttps.spring.entity.Ciudad;
import ttps.spring.service.GenericService;
import ttps.spring.service.CiudadService;

@RestController
@RequestMapping("/api/ciudades")
public class CiudadController extends GenericController<Ciudad, Long> {

    private final CiudadService service;

    public CiudadController(CiudadService service) {
        this.service = service;
    }

    @Override
    protected GenericService<Ciudad, Long> getService() {
        return service;
    }

    @Override
    protected String getBasePath() {
        return "/api/Ciudades";
    }

    @Override
    protected Long getId(Ciudad entidad) {
        return entidad.getId();
    }

}