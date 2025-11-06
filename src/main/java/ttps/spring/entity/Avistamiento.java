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
    // private List<Byte[]> fotos;
    @ManyToOne
    private Mascota mascota;
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuarioAvistador;

    // probando asd
    public Avistamiento() {
    }

    public Avistamiento(LocalDate fecha, LocalDateTime hora, Coordenada coordenada, Mascota mascota,
            Usuario usuarioAvistador) {
        super(fecha, hora, coordenada);
        this.mascota = mascota;
        this.usuarioAvistador = usuarioAvistador;
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

    /*
     * public List<Byte[]> getFotos() {
     * return fotos;
     * }
     * 
     * public void setFotos(List<Byte[]> fotos) {
     * this.fotos = fotos;
     * }
     * 
     */

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public Usuario getUsuario_avistador() {
        return usuarioAvistador;
    }

    public void setUsuario_avistador(Usuario usuarioAvistador) {
        this.usuarioAvistador = usuarioAvistador;
    }
}
