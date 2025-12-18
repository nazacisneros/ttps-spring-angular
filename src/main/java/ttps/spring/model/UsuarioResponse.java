package ttps.spring.model;

import ttps.spring.entity.Barrio;
import ttps.spring.entity.Ciudad;
import ttps.spring.entity.Usuario;

public class UsuarioResponse {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private boolean esAdmin;
    private Long barrioId;
    private String barrioNombre;
    private Long ciudadId;
    private String ciudadNombre;

    public UsuarioResponse(Usuario usuario) {
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.apellido = usuario.getApellido();
        this.email = usuario.getEmail();
        this.telefono = usuario.getTelefono();
        this.esAdmin = usuario.isEsAdmin();

        Barrio barrio = usuario.getBarrio();
        if (barrio != null) {
            this.barrioId = barrio.getId();
            this.barrioNombre = barrio.getNombre();
            Ciudad ciudad = barrio.getCiudad();
            if (ciudad != null) {
                this.ciudadId = ciudad.getId();
                this.ciudadNombre = ciudad.getNombre();
            }
        }
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public boolean isEsAdmin() {
        return esAdmin;
    }

    public Long getBarrioId() {
        return barrioId;
    }

    public String getBarrioNombre() {
        return barrioNombre;
    }

    public Long getCiudadId() {
        return ciudadId;
    }

    public String getCiudadNombre() {
        return ciudadNombre;
    }
}
