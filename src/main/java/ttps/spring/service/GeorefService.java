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

    public UbicacionResponse obtenerUbicacion(Double lat, Double lon) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(GEOREF_API_URL)
                    .queryParam("lat", lat)
                    .queryParam("lon", lon)
                    .toUriString();

            GeorefApiResponse response = restTemplate.getForObject(url, GeorefApiResponse.class);

            if (response != null) {
                System.out.println("  response.getUbicacion(): " + response.getUbicacion());

                if (response.getUbicacion() != null) {
                    GeorefUbicacion ubi = response.getUbicacion();

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
                    return ubicacion;
                }
            }

            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private UbicacionResponse convertirAUbicacion(GeorefUbicacion ubicacion) {
        UbicacionResponse response = new UbicacionResponse();

        String provinciaNombre = null;
        if (ubicacion.getProvincia() != null && ubicacion.getProvincia().getNombre() != null) {
            provinciaNombre = ubicacion.getProvincia().getNombre();
            System.out.println("  Provincia: " + provinciaNombre);
            response.setProvincia(provinciaNombre);
        }

        if (ubicacion.getMunicipio() != null && ubicacion.getMunicipio().getNombre() != null
                && provinciaNombre != null) {
            String ciudadBase = ubicacion.getMunicipio().getNombre();
            String ciudadNombreUnico = ciudadBase + ", " + provinciaNombre;
            response.setCiudad(ciudadNombreUnico);
        } else if (ubicacion.getLocalidadCensal() != null && ubicacion.getLocalidadCensal().getNombre() != null
                && provinciaNombre != null) {
            String ciudadBase = ubicacion.getLocalidadCensal().getNombre();
            String ciudadNombreUnico = ciudadBase + ", " + provinciaNombre;
            response.setCiudad(ciudadNombreUnico);
        } else {
            System.out.println(
                    "  No se pudo obtener nombre de ciudad ");
        }

        if (ubicacion.getDepartamento() != null && ubicacion.getDepartamento().getNombre() != null) {
            String barrioNombre = ubicacion.getDepartamento().getNombre();
            response.setBarrio(barrioNombre);
        } else {
            System.out.println(" No se pudo obtener nombre de barrio");
        }

        return response;
    }
}
