package ttps.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ttps.spring.entity.*;
import ttps.spring.exception.publicacion.PublicacionNoEncontradaException;
import ttps.spring.exception.publicacion.PublicacionValidationException;
import ttps.spring.model.ErrorResponse;
import ttps.spring.repository.BarrioRepository;
import ttps.spring.repository.CiudadRepository;
import ttps.spring.service.GenericService;
import ttps.spring.service.PublicacionService;
import ttps.spring.service.MascotaService;
import ttps.spring.service.BarrioService;
import ttps.spring.service.UsuarioService;
import ttps.spring.service.GeorefService;
import ttps.spring.dto.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/publicaciones")
public class PublicacionController extends GenericController<Publicacion, Long> {

        private final PublicacionService publicacionService;
        private final MascotaService mascotaService;
        private final BarrioService barrioService;
        private final UsuarioService usuarioService;
        private final GeorefService georefService;
        private final CiudadRepository ciudadRepository;
        private final BarrioRepository barrioRepository;

        public PublicacionController(PublicacionService service, MascotaService mascotaService,
                        BarrioService barrioService, UsuarioService usuarioService,
                        GeorefService georefService, CiudadRepository ciudadRepository,
                        BarrioRepository barrioRepository) {
                this.publicacionService = service;
                this.mascotaService = mascotaService;
                this.barrioService = barrioService;
                this.usuarioService = usuarioService;
                this.georefService = georefService;
                this.ciudadRepository = ciudadRepository;
                this.barrioRepository = barrioRepository;
        }

        @Override
        protected GenericService<Publicacion, Long> getService() {
                return publicacionService;
        }

        @GetMapping("/filtradas")
        public ResponseEntity<List<PublicacionListDto>> obtenerPublicacionesFiltradas(
                        @RequestParam(required = false) Long ciudadId,
                        @RequestParam(required = false) Long barrioId,
                        @RequestParam(required = false) String tamanio,
                        @RequestParam(required = false) String color,
                        @RequestParam(required = false) String estado) {

                List<Publicacion> publicaciones = publicacionService.listar();

                if (ciudadId != null) {
                        publicaciones = publicaciones.stream()
                                        .filter(p -> {
                                                if (p.getCoordenada() == null)
                                                        return false;
                                                try {
                                                        double lat = Double.parseDouble(p.getCoordenada().getLatitud());
                                                        double lng = Double
                                                                        .parseDouble(p.getCoordenada().getLongitud());
                                                        UbicacionResponse ubicacion = georefService
                                                                        .obtenerUbicacion(lat, lng);
                                                        if (ubicacion != null && ubicacion.getCiudad() != null) {
                                                                Ciudad ciudad = ciudadRepository
                                                                                .findByNombre(ubicacion.getCiudad())
                                                                                .orElse(null);
                                                                return ciudad != null
                                                                                && ciudad.getId().equals(ciudadId);
                                                        }
                                                } catch (Exception e) {
                                                        // Si hay error, no se incluye en el filtro
                                                }
                                                return false;
                                        })
                                        .collect(Collectors.toList());
                }

                if (barrioId != null) {
                        publicaciones = publicaciones.stream()
                                        .filter(p -> {
                                                if (p.getCoordenada() == null)
                                                        return false;
                                                try {
                                                        double lat = Double.parseDouble(p.getCoordenada().getLatitud());
                                                        double lng = Double
                                                                        .parseDouble(p.getCoordenada().getLongitud());
                                                        UbicacionResponse ubicacion = georefService
                                                                        .obtenerUbicacion(lat, lng);
                                                        if (ubicacion != null && ubicacion.getBarrio() != null) {
                                                                Ciudad ciudad = null;
                                                                if (ubicacion.getCiudad() != null) {
                                                                        ciudad = ciudadRepository
                                                                                        .findByNombre(ubicacion
                                                                                                        .getCiudad())
                                                                                        .orElse(null);
                                                                }
                                                                if (ciudad != null) {
                                                                        Barrio barrio = barrioRepository
                                                                                        .findByNombreAndCiudad(ubicacion
                                                                                                        .getBarrio(),
                                                                                                        ciudad)
                                                                                        .orElse(null);
                                                                        return barrio != null && barrio.getId()
                                                                                        .equals(barrioId);
                                                                }
                                                        }
                                                } catch (Exception e) {
                                                        // Si hay error, no se incluye en el filtro
                                                }
                                                return false;
                                        })
                                        .collect(Collectors.toList());
                }

                if (tamanio != null && !tamanio.isEmpty()) {
                        publicaciones = publicaciones.stream()
                                        .filter(p -> p.getMascota() != null
                                                        && tamanio.equals(p.getMascota().getTamanio()))
                                        .collect(Collectors.toList());
                }

                if (color != null && !color.isEmpty()) {
                        publicaciones = publicaciones.stream()
                                        .filter(p -> p.getMascota() != null && color.equals(p.getMascota().getColor()))
                                        .collect(Collectors.toList());
                }

                if (estado != null && !estado.isEmpty()) {
                        publicaciones = publicaciones.stream()
                                        .filter(p -> p.getMascota() != null
                                                        && estado.equals(p.getMascota().getEstado()))
                                        .collect(Collectors.toList());
                }

                List<PublicacionListDto> dtos = publicaciones.stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList());

                return ResponseEntity.ok(dtos);
        }

        private PublicacionListDto convertToDto(Publicacion publicacion) {
                Long ciudadId = null;
                Long barrioId = null;

                if (publicacion.getCoordenada() != null) {
                        try {
                                double lat = Double.parseDouble(publicacion.getCoordenada().getLatitud());
                                double lng = Double.parseDouble(publicacion.getCoordenada().getLongitud());

                                UbicacionResponse ubicacion = georefService.obtenerUbicacion(lat, lng);
                                if (ubicacion != null) {
                                        if (ubicacion.getCiudad() != null) {
                                                Ciudad ciudad = ciudadRepository.findByNombre(ubicacion.getCiudad())
                                                                .orElse(null);
                                                if (ciudad != null) {
                                                        ciudadId = ciudad.getId();
                                                }
                                        }
                                        if (ubicacion.getBarrio() != null && ciudadId != null) {
                                                Ciudad ciudad = ciudadRepository.findById(ciudadId).orElse(null);
                                                if (ciudad != null) {
                                                        Barrio barrio = barrioRepository.findByNombreAndCiudad(
                                                                        ubicacion.getBarrio(), ciudad).orElse(null);
                                                        if (barrio != null) {
                                                                barrioId = barrio.getId();
                                                        }
                                                }
                                        }
                                }
                        } catch (Exception e) {
                                System.out.println("Error calculando ubicación para publicación " + publicacion.getId()
                                                + ": " + e.getMessage());
                        }
                }

                return new PublicacionListDto(
                                publicacion.getId(),
                                publicacion.getFecha(),
                                publicacion.getHora(),
                                publicacion.getEstado(),
                                publicacion.getMascota().getNombre(),
                                publicacion.getMascota().getTamanio(),
                                publicacion.getMascota().getColor(),
                                publicacion.getMascota().getDescripcion(),
                                publicacion.getMascota().getEstado(),
                                publicacion.getCoordenada().getLatitud(),
                                publicacion.getCoordenada().getLongitud(),
                                publicacion.getMascota().getId(),
                                publicacion.getMascota().getUsuario() != null
                                                ? publicacion.getMascota().getUsuario().getId()
                                                : null,
                                ciudadId,
                                barrioId);
        }

        @Override
        protected String getBasePath() {
                return "/api/publicaciones";
        }

        @Override
        protected Long getId(Publicacion entidad) {
                return entidad.getId();
        }

        @GetMapping("/{id}/detalle")
        public ResponseEntity<PublicacionListDto> obtenerPublicacionDetalle(@PathVariable Long id) {

                Publicacion publicacion = publicacionService.obtener(id)
                                .orElseThrow(() -> new RuntimeException("Publicacion no encontrada con ID: " + id));

                if (publicacion.getMascota() != null) {
                        System.out.println("  Mascota: " + publicacion.getMascota().getNombre());
                } else {
                        System.out.println(" Publicacion sin mascota asociada");
                }

                PublicacionListDto dto = convertToDto(publicacion);

                return ResponseEntity.ok(dto);
        }

        @PostMapping("/con-mascota")
        public ResponseEntity<PublicacionResponse> crearPublicacionConMascota(
                        @RequestBody CrearPublicacionRequest request,
                        Authentication authentication) {
                try {
                        String email = authentication.getName();
                        Usuario usuario = usuarioService.buscarPorEmail(email)
                                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                        Coordenada coordenada = new Coordenada(
                                        String.valueOf(request.getCoordenadas().getLongitud()),
                                        String.valueOf(request.getCoordenadas().getLatitud()));

                        Mascota mascota = new Mascota(
                                        request.getMascota().getNombre(),
                                        request.getMascota().getTamanio(),
                                        request.getMascota().getColor(),
                                        request.getMascota().getDescripcion(),
                                        request.getMascota().getEstado());

                        Ciudad ciudad = null;
                        Barrio barrio = null;

                        if (request.getCoordenadas() != null) {

                                UbicacionResponse ubicacion = georefService.obtenerUbicacion(
                                                request.getCoordenadas().getLatitud(),
                                                request.getCoordenadas().getLongitud());

                                if (ubicacion != null) {

                                        if (ubicacion.getCiudad() != null) {
                                                ciudad = buscarOCrearCiudad(ubicacion.getCiudad());

                                                if (ubicacion.getBarrio() != null) {
                                                        barrio = buscarOCrearBarrio(ubicacion.getBarrio(), ciudad);
                                                }
                                        }
                                } else {
                                        if (request.getBarrioId() != null) {
                                                barrio = barrioService.obtener(request.getBarrioId())
                                                                .orElse(null);
                                                if (barrio != null && barrio.getCiudad() != null) {
                                                        ciudad = barrio.getCiudad();
                                                }
                                        }
                                }
                        }

                        if (ciudad != null) {
                                mascota.setCiudad(ciudad);
                        }
                        if (barrio != null) {
                                mascota.setBarrio(barrio);
                        }

                        Mascota savedMascota = mascotaService.crearMascota(usuario.getId(), mascota);

                        String horaSinZ = request.getHora().replace("Z", "");
                        LocalDateTime fechaHora = LocalDateTime.parse(horaSinZ);
                        Publicacion publicacion = new Publicacion(
                                        LocalDate.parse(request.getFecha()),
                                        fechaHora,
                                        coordenada,
                                        request.getEstado(),
                                        savedMascota,
                                        usuario);

                        Publicacion savedPublicacion = publicacionService.crear(publicacion);

                        PublicacionResponse response = new PublicacionResponse(
                                        savedPublicacion.getId(),
                                        "Publicación creada exitosamente");

                        return ResponseEntity.created(URI.create("/api/publicaciones/" + savedPublicacion.getId()))
                                        .body(response);

                } catch (Exception e) {
                        throw new RuntimeException("Error creating publication with pet: " + e.getMessage());
                }
        }

        private Ciudad buscarOCrearCiudad(String nombreCiudad) {

                Optional<Ciudad> ciudadExistente = ciudadRepository.findByNombre(nombreCiudad);

                if (ciudadExistente.isPresent()) {
                        return ciudadExistente.get();
                } else {
                        Ciudad ciudad = new Ciudad();
                        ciudad.setNombre(nombreCiudad);
                        Ciudad guardada = ciudadRepository.save(ciudad);
                        return guardada;
                }
        }

        private Barrio buscarOCrearBarrio(String nombreBarrio, Ciudad ciudad) {
                Optional<Barrio> barrioExistente = barrioRepository.findByNombreAndCiudad(nombreBarrio, ciudad);

                if (barrioExistente.isPresent()) {
                        return barrioExistente.get();
                } else {
                        Barrio barrio = new Barrio();
                        barrio.setNombre(nombreBarrio);
                        barrio.setCiudad(ciudad);
                        Barrio guardado = barrioRepository.save(barrio);
                        Ciudad ciudadVerificada = ciudadRepository.findById(ciudad.getId()).orElse(null);
                        if (ciudadVerificada != null) {
                                System.out.println("Ciudad en BD: '" + ciudadVerificada.getNombre() + "'");
                        }

                        return guardado;
                }
        }

        @DeleteMapping("/{id}/usuario/{usuarioId}")
        public ResponseEntity<Void> eliminarPublicacion(@PathVariable Long id, @PathVariable Long usuarioId,
                        Authentication authentication) {

                String email = authentication.getName();
                Usuario usuarioAutenticado = usuarioService.buscarPorEmail(email)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                Publicacion publicacion = publicacionService.obtener(id)
                                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

                if (publicacion.getUsuario_publicador() == null) {
                        throw new RuntimeException("La publicación no tiene usuario publicador asignado");
                }

                if (!publicacion.getUsuario_publicador().getId().equals(usuarioAutenticado.getId())) {
                        throw new RuntimeException("No tienes permiso para eliminar esta publicación. " +
                                        "Publicador: " + publicacion.getUsuario_publicador().getId() +
                                        ", Solicitante: " + usuarioAutenticado.getId());
                }

                publicacionService.eliminar(id);

                return ResponseEntity.noContent().build();
        }

        @PutMapping("/{id}/actualizar")
        public ResponseEntity<PublicacionResponse> actualizarPublicacion(@PathVariable Long id,
                        @RequestBody CrearPublicacionRequest request,
                        Authentication authentication) {
                try {

                        String email = authentication.getName();
                        Usuario usuarioAutenticado = usuarioService.buscarPorEmail(email)
                                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                        Publicacion publicacion = publicacionService.obtener(id)
                                        .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

                        if (publicacion.getUsuario_publicador() == null ||
                                        !publicacion.getUsuario_publicador().getId()
                                                        .equals(usuarioAutenticado.getId())) {
                                throw new RuntimeException("No tienes permiso para editar esta publicación");
                        }

                        Mascota mascota = publicacion.getMascota();
                        if (request.getMascota() != null) {
                                mascota.setNombre(request.getMascota().getNombre());
                                mascota.setTamanio(request.getMascota().getTamanio());
                                mascota.setColor(request.getMascota().getColor());
                                mascota.setDescripcion(request.getMascota().getDescripcion());
                                mascota.setEstado(request.getMascota().getEstado()); // ✅ Cambiar estado de la mascota
                                mascotaService.actualizar(mascota.getId(), mascota);
                        }

                        if (request.getCoordenadas() != null) {
                                Coordenada coordenada = publicacion.getCoordenada();
                                coordenada.setLatitud(String.valueOf(request.getCoordenadas().getLatitud()));
                                coordenada.setLongitud(String.valueOf(request.getCoordenadas().getLongitud()));
                        }

                        Publicacion actualizada = publicacionService.actualizar(publicacion.getId(), publicacion);
                        PublicacionResponse response = new PublicacionResponse(
                                        actualizada.getId(),
                                        "Publicación actualizada exitosamente");

                        return ResponseEntity.ok(response);

                } catch (Exception e) {
                        throw new RuntimeException("Error al actualizar la publicación: " + e.getMessage());
                }
        }

        @ExceptionHandler(PublicacionNoEncontradaException.class)
        public ResponseEntity<ErrorResponse> handlePublicacionNoEncontradaException(
                        PublicacionNoEncontradaException ex) {
                ErrorResponse error = new ErrorResponse(
                                HttpStatus.NOT_FOUND.value(),
                                ex.getMessage(),
                                System.currentTimeMillis());
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(PublicacionValidationException.class)
        public ResponseEntity<ErrorResponse> handlePublicacionValidationException(
                        PublicacionValidationException ex) {
                ErrorResponse error = new ErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                ex.getMessage(),
                                System.currentTimeMillis());
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
}
