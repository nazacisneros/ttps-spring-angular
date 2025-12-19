package ttps.spring.dto;

public class BarrioDto {
    private Long id;
    private String nombre;
    private Long ciudadId;
    private String ciudadNombre;

    public BarrioDto() {
    }

    public BarrioDto(Long id, String nombre, Long ciudadId, String ciudadNombre) {
        this.id = id;
        this.nombre = nombre;
        this.ciudadId = ciudadId;
        this.ciudadNombre = ciudadNombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getCiudadId() {
        return ciudadId;
    }

    public void setCiudadId(Long ciudadId) {
        this.ciudadId = ciudadId;
    }

    public String getCiudadNombre() {
        return ciudadNombre;
    }

    public void setCiudadNombre(String ciudadNombre) {
        this.ciudadNombre = ciudadNombre;
    }
}

