package ttps.spring.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;

import java.time.LocalDate;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class Registro {

    protected LocalDate fecha;
    protected LocalDateTime hora;

    @OneToOne(cascade = CascadeType.ALL)
    protected Coordenada coordenada;

    public Registro() {}

    public Registro(LocalDate fecha, LocalDateTime hora, Coordenada coordenada) {
        this.fecha = fecha;
        this.hora = hora;
        this.coordenada = coordenada;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalDateTime getHora() {
        return hora;
    }

    public void setHora(LocalDateTime hora) {
        this.hora = hora;
    }

    public Coordenada getCoordenada() {
        return coordenada;
    }

    public void setCoordenada(Coordenada coordenada) {
        this.coordenada = coordenada;
    }
}
