package ttps.spring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ttps.spring.entity.*;
import ttps.spring.service.GenericService;
import ttps.spring.service.PublicacionService;
import ttps.spring.service.MascotaService;
import ttps.spring.service.BarrioService;
import ttps.spring.service.UsuarioService;
import ttps.spring.dto.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/publicaciones")
public class PublicacionController extends GenericController<Publicacion, Long> {

        private final PublicacionService publicacionService;
        private final MascotaService mascotaService;
        private final BarrioService barrioService;
        private final UsuarioService usuarioService;

        public PublicacionController(PublicacionService service, MascotaService mascotaService,
                        BarrioService barrioService, UsuarioService usuarioService) {
                this.publicacionService = service;
                this.mascotaService = mascotaService;
                this.barrioService = barrioService;
                this.usuarioService = usuarioService;
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
                                        .filter(p -> p.getMascota() != null &&
                                                        p.getMascota().getUsuario() != null &&
                                                        p.getMascota().getUsuario().getBarrio() != null &&
                                                        p.getMascota().getUsuario().getBarrio().getCiudad() != null &&
                                                        p.getMascota().getUsuario().getBarrio().getCiudad().getId()
                                                                        .equals(ciudadId))
                                        .collect(Collectors.toList());
                }

                if (barrioId != null) {
                        publicaciones = publicaciones.stream()
                                        .filter(p -> p.getMascota() != null &&
                                                        p.getMascota().getUsuario() != null &&
                                                        p.getMascota().getUsuario().getBarrio() != null &&
                                                        p.getMascota().getUsuario().getBarrio().getId()
                                                                        .equals(barrioId))
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
                                        .filter(p -> estado.equals(p.getEstado()))
                                        .collect(Collectors.toList());
                }

                // DTO para poder enviar solo los datos necesarios
                List<PublicacionListDto> dtos = publicaciones.stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList());

                return ResponseEntity.ok(dtos);
        }

        private PublicacionListDto convertToDto(Publicacion publicacion) {
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
                                publicacion.getCoordenada().getLongitud());
        }

        @Override
        protected String getBasePath() {
                return "/api/publicaciones";
        }

        @Override
        protected Long getId(Publicacion entidad) {
                return entidad.getId();
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

                        mascota.setUsuario(usuario);

                        Mascota savedMascota = mascotaService.crear(mascota);

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
}
