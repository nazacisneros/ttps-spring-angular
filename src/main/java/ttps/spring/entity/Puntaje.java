package ttps.spring.entity;

import jakarta.persistence.*;

@Entity
public class Puntaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long puntaje_id;
    private int puntos;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ranking_id")
    private Ranking ranking;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Usuario_id")
    private Usuario usuario_puntaje;

    public Puntaje() {}

    public Puntaje(int puntos, Ranking ranking, Usuario usuario) {
        this.puntos = puntos;
        this.ranking = ranking;
        this.usuario_puntaje = usuario;
    }
    public int getPuntos() {
        return puntos;
    }
    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public void incrementarPuntos(int puntos) {
        this.puntos += puntos;
    }

    public void setRanking(Ranking ranking) {
        this.ranking = ranking;
    }

    public Ranking getRanking() {
        return this.ranking;
    }

    public Long getId() {
        return this.puntaje_id;
    }
}
