package ttps.spring.entity;

import jakarta.persistence.*;

@Entity
public class Ciudad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ciudad_id;
    private String nombre;
    @OneToOne(cascade = CascadeType.ALL)
    private Coordenada coordenada;

    public Ciudad() {}

    public Ciudad(String nombre, Coordenada coordenada) {
        this.nombre = nombre;
        this.coordenada = coordenada;
    };

    public Long getId() {
        return this.ciudad_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Coordenada getCoordenada() {
        return coordenada;
    }

    public void setCoordenada(Coordenada coordenada) {
        this.coordenada = coordenada;
    }
}

