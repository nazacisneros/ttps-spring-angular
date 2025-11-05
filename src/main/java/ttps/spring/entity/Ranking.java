package ttps.spring.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Ranking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ranking_id;
    private String nombre;
    private int puntaje;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ranking_id")
    private List<Medalla> medallas;

    public Ranking() {}

    public Ranking(String nombre, int puntaje) {
        this.nombre = nombre;
        this.puntaje = puntaje;
        this.medallas = new ArrayList<>();
    }

    public Long getId() {
        return this.ranking_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public List<Medalla> getMedallas() {
        return medallas;
    }

    public void agregarMedalla(Medalla medalla) {
        this.medallas.add(medalla);
    }

    public void incrementarPuntos(int puntos) {
        this.puntaje += puntos;
    }
}
