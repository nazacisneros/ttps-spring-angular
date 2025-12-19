package ttps.spring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ttps.spring.entity.*;
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
                Long ciudadId = null;
                Long barrioId = null;

                // Obtener ciudadId y barrioId de la coordenada o del usuario
                if (publicacion.getMascota() != null && publicacion.getMascota().getUsuario() != null) {
                        Usuario usuario = publicacion.getMascota().getUsuario();
                        if (usuario.getBarrio() != null) {
                                barrioId = usuario.getBarrio().getId();
                                if (usuario.getBarrio().getCiudad() != null) {
                                        ciudadId = usuario.getBarrio().getCiudad().getId();
                                }
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
                                publicacion.getMascota().getUsuario() != null ? publicacion.getMascota().getUsuario().getId() : null,
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
                System.out.println("DEBUG - GET /api/publicaciones/" + id + "/detalle");

                Publicacion publicacion = publicacionService.obtener(id)
                                .orElseThrow(() -> new RuntimeException("Publicación no encontrada con ID: " + id));

                System.out.println("  Publicación encontrada ID: " + publicacion.getId());

                if (publicacion.getMascota() != null) {
                        System.out.println("  Mascota: " + publicacion.getMascota().getNombre());
                } else {
                        System.out.println("  WARNING: Publicación sin mascota asociada!");
                }

                // Crear DTO con información completa
                PublicacionListDto dto = convertToDto(publicacion);

                System.out.println("  DTO creado:");
                System.out.println("    - nombreMascota: " + dto.getNombreMascota());
                System.out.println("    - ciudadId: " + dto.getCiudadId());
                System.out.println("    - barrioId: " + dto.getBarrioId());
                System.out.println("    - latitud: " + dto.getLatitud());
                System.out.println("    - longitud: " + dto.getLongitud());

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

                        // 🆕 OBTENER CIUDAD Y BARRIO DESDE GEOREF API
                        Ciudad ciudad = null;
                        Barrio barrio = null;

                        if (request.getCoordenadas() != null) {

                            System.out.println("DEBUG - Obteniendo ubicación desde Georef API");
                            System.out.println("  Coordenadas: lat=" + request.getCoordenadas().getLatitud() +
                                             ", lng=" + request.getCoordenadas().getLongitud());

                            UbicacionResponse ubicacion = georefService.obtenerUbicacion(
                                    request.getCoordenadas().getLatitud(),
                                    request.getCoordenadas().getLongitud()
                            );

                            if (ubicacion != null) {
                                System.out.println("DEBUG - Respuesta de Georef:");
                                System.out.println("  Ciudad obtenida: " + ubicacion.getCiudad());
                                System.out.println("  Barrio obtenido: " + ubicacion.getBarrio());
                                System.out.println("  Provincia obtenida: " + ubicacion.getProvincia());

                                if (ubicacion.getCiudad() != null) {
                                    // Buscar o crear Ciudad
                                    ciudad = buscarOCrearCiudad(ubicacion.getCiudad());
                                    System.out.println("DEBUG - Ciudad guardada en BD: " + ciudad.getNombre() + " (ID: " + ciudad.getId() + ")");

                                    // Buscar o crear Barrio
                                    if (ubicacion.getBarrio() != null) {
                                        barrio = buscarOCrearBarrio(ubicacion.getBarrio(), ciudad);
                                        System.out.println("DEBUG - Barrio guardado en BD: " + barrio.getNombre() + " (ID: " + barrio.getId() + ")");
                                    }
                                }
                            } else {
                                System.out.println("WARNING - No se pudo obtener ubicación desde Georef, usando barrio del request");
                                // Fallback: usar barrioId del request si viene
                                if (request.getBarrioId() != null) {
                                    barrio = barrioService.obtener(request.getBarrioId())
                                            .orElse(null);
                                    if (barrio != null && barrio.getCiudad() != null) {
                                        ciudad = barrio.getCiudad();
                                    }
                                }
                            }
                        }

                        // Asignar ciudad y barrio a la mascota
                        if (ciudad != null) {
                            mascota.setCiudad(ciudad);
                        }
                        if (barrio != null) {
                            mascota.setBarrio(barrio);
                        }

                        // Usar el método específico crearMascota que garantiza la asignación del usuario
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

        // Método auxiliar para buscar o crear Ciudad
        private Ciudad buscarOCrearCiudad(String nombreCiudad) {
                System.out.println("DEBUG - buscarOCrearCiudad llamado con: '" + nombreCiudad + "'");

                Optional<Ciudad> ciudadExistente = ciudadRepository.findByNombre(nombreCiudad);

                if (ciudadExistente.isPresent()) {
                        System.out.println("DEBUG - Ciudad ya existe en BD: " + ciudadExistente.get().getNombre() + " (ID: " + ciudadExistente.get().getId() + ")");
                        return ciudadExistente.get();
                } else {
                        System.out.println("DEBUG - Creando nueva ciudad: '" + nombreCiudad + "'");
                        Ciudad ciudad = new Ciudad();
                        ciudad.setNombre(nombreCiudad);
                        Ciudad guardada = ciudadRepository.save(ciudad);
                        System.out.println("DEBUG - Ciudad guardada: '" + guardada.getNombre() + "' (ID: " + guardada.getId() + ")");
                        return guardada;
                }
        }

        // Método auxiliar para buscar o crear Barrio
        private Barrio buscarOCrearBarrio(String nombreBarrio, Ciudad ciudad) {
                System.out.println("DEBUG - buscarOCrearBarrio llamado con:");
                System.out.println("  nombreBarrio: '" + nombreBarrio + "'");
                System.out.println("  ciudad: '" + ciudad.getNombre() + "' (ID: " + ciudad.getId() + ")");

                Optional<Barrio> barrioExistente = barrioRepository.findByNombreAndCiudad(nombreBarrio, ciudad);

                if (barrioExistente.isPresent()) {
                        System.out.println("DEBUG - Barrio ya existe en BD: " + barrioExistente.get().getNombre() + " (ID: " + barrioExistente.get().getId() + ")");
                        return barrioExistente.get();
                } else {
                        System.out.println("DEBUG - Creando nuevo barrio: '" + nombreBarrio + "'");
                        Barrio barrio = new Barrio();
                        barrio.setNombre(nombreBarrio);
                        barrio.setCiudad(ciudad);
                        Barrio guardado = barrioRepository.save(barrio);
                        System.out.println("DEBUG - Barrio guardado: '" + guardado.getNombre() + "' (ID: " + guardado.getId() + ")");

                        // Verificar que la ciudad no se haya modificado
                        System.out.println("DEBUG - Verificando que ciudad siga siendo: '" + ciudad.getNombre() + "' (ID: " + ciudad.getId() + ")");
                        Ciudad ciudadVerificada = ciudadRepository.findById(ciudad.getId()).orElse(null);
                        if (ciudadVerificada != null) {
                                System.out.println("DEBUG - Ciudad en BD después de guardar barrio: '" + ciudadVerificada.getNombre() + "'");
                        }

                        return guardado;
                }
        }

        @DeleteMapping("/{id}/usuario/{usuarioId}")
        public ResponseEntity<Void> eliminarPublicacion(@PathVariable Long id, @PathVariable Long usuarioId,
                        Authentication authentication) {
                System.out.println("DEBUG Controller - Eliminar publicación:");
                System.out.println("  PathVariable id (publicacionId): " + id);
                System.out.println("  PathVariable usuarioId: " + usuarioId);
                System.out.println("  Authentication.getName(): " + authentication.getName());

                // Obtener el email del usuario autenticado desde el token JWT
                String email = authentication.getName();
                Usuario usuarioAutenticado = usuarioService.buscarPorEmail(email)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                System.out.println("  Usuario autenticado ID: " + usuarioAutenticado.getId());

                // Obtener la publicación
                Publicacion publicacion = publicacionService.obtener(id)
                                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

                System.out.println("  Publicacion encontrada, estado: " + publicacion.getEstado());
                System.out.println("  Usuario publicador: " + publicacion.getUsuario_publicador());

                // Validar que el usuario autenticado sea el publicador
                if (publicacion.getUsuario_publicador() == null) {
                        throw new RuntimeException("La publicación no tiene usuario publicador asignado");
                }

                System.out.println("  ID del publicador: " + publicacion.getUsuario_publicador().getId());

                if (!publicacion.getUsuario_publicador().getId().equals(usuarioAutenticado.getId())) {
                        throw new RuntimeException("No tienes permiso para eliminar esta publicación. " +
                                        "Publicador: " + publicacion.getUsuario_publicador().getId() +
                                        ", Solicitante: " + usuarioAutenticado.getId());
                }

                System.out.println("  Validación OK - Eliminando publicación");

                // Eliminar la publicación
                publicacionService.eliminar(id);

                System.out.println("  Publicación eliminada exitosamente");

                return ResponseEntity.noContent().build();
        }

        @PutMapping("/{id}/actualizar")
        public ResponseEntity<PublicacionResponse> actualizarPublicacion(@PathVariable Long id,
                        @RequestBody CrearPublicacionRequest request,
                        Authentication authentication) {
                try {
                        System.out.println("DEBUG Controller - Actualizar publicación:");
                        System.out.println("  Publicación ID: " + id);

                        // Obtener el email del usuario autenticado desde el token JWT
                        String email = authentication.getName();
                        Usuario usuarioAutenticado = usuarioService.buscarPorEmail(email)
                                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                        System.out.println("  Usuario autenticado ID: " + usuarioAutenticado.getId());

                        // Obtener la publicación existente
                        Publicacion publicacion = publicacionService.obtener(id)
                                        .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

                        // Validar que el usuario autenticado sea el publicador
                        if (publicacion.getUsuario_publicador() == null ||
                                        !publicacion.getUsuario_publicador().getId()
                                                        .equals(usuarioAutenticado.getId())) {
                                throw new RuntimeException("No tienes permiso para editar esta publicación");
                        }

                        System.out.println("  Validación OK - Actualizando datos");

                        // Actualizar datos de la mascota
                        Mascota mascota = publicacion.getMascota();
                        if (request.getMascota() != null) {
                                mascota.setNombre(request.getMascota().getNombre());
                                mascota.setTamanio(request.getMascota().getTamanio());
                                mascota.setColor(request.getMascota().getColor());
                                mascota.setDescripcion(request.getMascota().getDescripcion());
                                mascota.setEstado(request.getMascota().getEstado());
                                mascotaService.actualizar(mascota.getId(), mascota);
                        }

                        // Actualizar coordenadas
                        if (request.getCoordenadas() != null) {
                                Coordenada coordenada = publicacion.getCoordenada();
                                coordenada.setLatitud(String.valueOf(request.getCoordenadas().getLatitud()));
                                coordenada.setLongitud(String.valueOf(request.getCoordenadas().getLongitud()));
                        }

                        // Actualizar estado de la publicación
                        if (request.getEstado() != null) {
                                publicacion.setEstado(request.getEstado());
                        }

                        // Guardar la publicación actualizada
                        Publicacion actualizada = publicacionService.actualizar(publicacion.getId(), publicacion);

                        System.out.println("  Publicación actualizada exitosamente");

                        PublicacionResponse response = new PublicacionResponse(
                                        actualizada.getId(),
                                        "Publicación actualizada exitosamente");

                        return ResponseEntity.ok(response);

                } catch (Exception e) {
                        System.err.println("Error actualizando publicación: " + e.getMessage());
                        throw new RuntimeException("Error al actualizar la publicación: " + e.getMessage());
                }
        }
}
