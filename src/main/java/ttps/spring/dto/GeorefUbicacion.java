package ttps.spring.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeorefUbicacion {
    private Double lat;
    private Double lon;
    private Provincia provincia;
    private Departamento departamento;
    private Municipio municipio;
    @JsonProperty("localidad_censal")
    private LocalidadCensal localidadCensal;

    public Double getLat() {
        return lat;
    }
    public void setLat(Double lat) {
        this.lat = lat;
    }
    public Double getLon() {
        return lon;
    }
    public void setLon(Double lon) {
        this.lon = lon;
    }
    public Provincia getProvincia() {
        return provincia;
    }
    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }
    public Departamento getDepartamento() {
        return departamento;
    }
    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
    public Municipio getMunicipio() {
        return municipio;
    }
    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }
    public LocalidadCensal getLocalidadCensal() {
        return localidadCensal;
    }
    public void setLocalidadCensal(LocalidadCensal localidadCensal) {
        this.localidadCensal = localidadCensal;
    }

    public static class Provincia {
        private String id;
        private String nombre;

        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getNombre() {
            return nombre;
        }
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
    }

    public static class Departamento {
        private String id;
        private String nombre;

        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getNombre() {
            return nombre;
        }
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
    }

    public static class Municipio {
        private String id;
        private String nombre;

        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getNombre() {
            return nombre;
        }
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
    }

    public static class LocalidadCensal {
        private String id;
        private String nombre;

        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getNombre() {
            return nombre;
        }
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
    }
}
