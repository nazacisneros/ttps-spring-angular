package ttps.spring.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Avistamiento extends Registro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long avistamiento_id;
    private String comentario;
    //private List<Byte[]> fotos;
    @ManyToOne
    private Mascota mascota;
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario_avistador;

    public Avistamiento() {}

    public Avistamiento(LocalDate fecha, LocalDateTime hora, Coordenada coordenada, Mascota mascota, Usuario usuario_publicador) {
        super(fecha, hora, coordenada);
        this.mascota = mascota;
        this.usuario_avistador = usuario_publicador;
    }

    public Long getId() {
        return avistamiento_id;
    }

    public void setId(Long id) {
        this.avistamiento_id = id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    /* public List<Byte[]> getFotos() {
        return fotos;
    }

    public void setFotos(List<Byte[]> fotos) {
        this.fotos = fotos;
    }

     */

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public Usuario getUsuario_publicador() {
        return usuario_avistador;
    }

    public void setUsuario_publicador(Usuario usuario_publicador) {
        this.usuario_avistador = usuario_publicador;
    }
}
