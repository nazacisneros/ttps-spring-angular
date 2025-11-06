package ttps.spring.controller;

import org.springframework.web.bind.annotation.*;
import ttps.spring.entity.Publicacion;
import ttps.spring.service.GenericService;
import ttps.spring.service.PublicacionService;

@RestController
@RequestMapping("/api/publicaciones")
public class PublicacionController extends GenericController<Publicacion, Long> {

    private final PublicacionService service;

    public PublicacionController(PublicacionService service) {
        this.service = service;
    }

    @Override
    protected GenericService<Publicacion, Long> getService() {
        return service;
    }

    @Override
    protected String getBasePath() {
        return "/api/publicaciones";
    }

    @Override
    protected Long getId(Publicacion entidad) {
        return entidad.getId();
    }

}