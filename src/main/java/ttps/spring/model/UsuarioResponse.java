package ttps.spring.model;

import ttps.spring.entity.Usuario;

public class UsuarioResponse {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private boolean esAdmin;

    public UsuarioResponse(Usuario usuario) {
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.apellido = usuario.getApellido();
        this.email = usuario.getEmail();
        this.esAdmin = usuario.isEsAdmin();
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

    public boolean isEsAdmin() {
        return esAdmin;
    }
}