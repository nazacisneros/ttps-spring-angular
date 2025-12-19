package ttps.spring.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ttps.spring.dto.GeorefApiResponse;
import ttps.spring.dto.GeorefUbicacion;
import ttps.spring.dto.UbicacionResponse;

@Service
public class GeorefService {

    private final RestTemplate restTemplate;
    private static final String GEOREF_API_URL = "https://apis.datos.gob.ar/georef/api/ubicacion";

    public GeorefService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Obtiene la ubicación (ciudad/barrio) a partir de coordenadas
     * @param lat Latitud
     * @param lon Longitud
     * @return UbicacionResponse con ciudad, barrio y provincia
     */
    public UbicacionResponse obtenerUbicacion(Double lat, Double lon) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(GEOREF_API_URL)
                    .queryParam("lat", lat)
                    .queryParam("lon", lon)
                    .toUriString();

            System.out.println("DEBUG - Llamando a Georef API: " + url);

            GeorefApiResponse response = restTemplate.getForObject(url, GeorefApiResponse.class);

            System.out.println("DEBUG - Respuesta RAW de Georef API:");
            System.out.println("  response: " + response);

            if (response != null) {
                System.out.println("  response.getUbicacion(): " + response.getUbicacion());

                if (response.getUbicacion() != null) {
                    GeorefUbicacion ubi = response.getUbicacion();
                    System.out.println("  Ubicacion completa:");
                    System.out.println("    - Provincia: " + ubi.getProvincia());
                    System.out.println("    - Departamento: " + ubi.getDepartamento());
                    System.out.println("    - Municipio: " + ubi.getMunicipio());
                    System.out.println("    - LocalidadCensal: " + ubi.getLocalidadCensal());

                    if (ubi.getProvincia() != null) {
                        System.out.println("      Provincia.nombre: " + ubi.getProvincia().getNombre());
                    }
                    if (ubi.getDepartamento() != null) {
                        System.out.println("      Departamento.nombre: " + ubi.getDepartamento().getNombre());
                    }
                    if (ubi.getMunicipio() != null) {
                        System.out.println("      Municipio.nombre: " + ubi.getMunicipio().getNombre());
                    }
                    if (ubi.getLocalidadCensal() != null) {
                        System.out.println("      LocalidadCensal.nombre: " + ubi.getLocalidadCensal().getNombre());
                    }

                    UbicacionResponse ubicacion = convertirAUbicacion(response.getUbicacion());
                    System.out.println("  Resultado de conversión:");
                    System.out.println("    Ciudad: " + ubicacion.getCiudad());
                    System.out.println("    Barrio: " + ubicacion.getBarrio());
                    System.out.println("    Provincia: " + ubicacion.getProvincia());
                    return ubicacion;
                }
            }

            System.out.println("WARNING - Georef API no devolvió ubicación");
            return null;

        } catch (Exception e) {
            System.err.println("ERROR - Error al llamar a Georef API: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private UbicacionResponse convertirAUbicacion(GeorefUbicacion ubicacion) {
        UbicacionResponse response = new UbicacionResponse();

        System.out.println("DEBUG - Convirtiendo ubicación a DTO:");

        // Ciudad (puede venir de municipio o localidad_censal)
        if (ubicacion.getMunicipio() != null && ubicacion.getMunicipio().getNombre() != null) {
            String ciudadNombre = ubicacion.getMunicipio().getNombre();
            System.out.println("  Asignando ciudad desde Municipio: " + ciudadNombre);
            response.setCiudad(ciudadNombre);
        } else if (ubicacion.getLocalidadCensal() != null && ubicacion.getLocalidadCensal().getNombre() != null) {
            String ciudadNombre = ubicacion.getLocalidadCensal().getNombre();
            System.out.println("  Asignando ciudad desde LocalidadCensal: " + ciudadNombre);
            response.setCiudad(ciudadNombre);
        } else {
            System.out.println("  WARNING - No se pudo obtener nombre de ciudad (Municipio y LocalidadCensal son null)");
        }

        // Barrio (puede venir de departamento o comuna)
        if (ubicacion.getDepartamento() != null && ubicacion.getDepartamento().getNombre() != null) {
            String barrioNombre = ubicacion.getDepartamento().getNombre();
            System.out.println("  Asignando barrio desde Departamento: " + barrioNombre);
            response.setBarrio(barrioNombre);
        } else {
            System.out.println("  WARNING - No se pudo obtener nombre de barrio (Departamento es null)");
        }

        // Provincia
        if (ubicacion.getProvincia() != null && ubicacion.getProvincia().getNombre() != null) {
            String provinciaNombre = ubicacion.getProvincia().getNombre();
            System.out.println("  Asignando provincia: " + provinciaNombre);
            response.setProvincia(provinciaNombre);
        } else {
            System.out.println("  WARNING - No se pudo obtener nombre de provincia");
        }

        System.out.println("DEBUG - DTO final:");
        System.out.println("  response.getCiudad(): " + response.getCiudad());
        System.out.println("  response.getBarrio(): " + response.getBarrio());
        System.out.println("  response.getProvincia(): " + response.getProvincia());

        return response;
    }
}

