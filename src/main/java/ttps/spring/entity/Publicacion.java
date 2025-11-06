package ttps.spring.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Publicacion extends Registro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    private String estado;
    @OneToOne
    @JoinColumn(name = "mascota_id")
    private Mascota mascota;
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuarioPublicador;

    public Publicacion() {}

    public Publicacion(LocalDate fecha, LocalDateTime hora, Coordenada coordenada, String estado, Mascota mascota, Usuario usuario) {
        super(fecha, hora, coordenada);
        this.estado = estado;
        this.mascota = mascota;
        this.usuarioPublicador = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Usuario getUsuario_publicador() {
        return usuarioPublicador;
    }

    public void setUsuario_publicador(Usuario usuarioPublicador) {
        this.usuarioPublicador = usuarioPublicador;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }
}