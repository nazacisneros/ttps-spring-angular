package ttps.spring.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Mascota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long mascota_id;
    private String nombre;
    private String tamanio;
    private String color;
    private String descripcion;
    private String estado;
    @Transient
    private List<Byte[]> fotos;
    @OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL)
    private List<Avistamiento> avistamientos;
    @OneToOne
    private Usuario usuario;

    public Mascota() {}

    public Mascota(String nombre, String tamanio, String color, String descripcion, String estado) {
        this.nombre = nombre;
        this.tamanio = tamanio;
        this.color = color;
        this.descripcion = descripcion;
        this.fotos = new ArrayList<>();
        this.avistamientos = new ArrayList<>();
    }

    public Long getId() {
        return this.mascota_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTamanio() {
        return tamanio;
    }

    public void setTamanio(String tamanio) {
        this.tamanio = tamanio;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Byte[]> getFotos() {
        return fotos;
    }

    public void setFotos(List<Byte[]> fotos) {
        this.fotos = fotos;
    }

    public List<Avistamiento> getAvistamientos() {
        return avistamientos;
    }

    public void setAvistamientos(List<Avistamiento> avistamientos) {
        this.avistamientos = avistamientos;
    }

    public void agregarAvistamiento(Avistamiento avistamiento) {
        this.avistamientos.add(avistamiento);
    }

    public void eliminarAvistamiento(Long id_avistamiento) {
        this.avistamientos.remove(id_avistamiento);
    }

    public void setUsuario(Usuario usuario) {this.usuario=usuario;}

    public Usuario getUsuario() {return this.usuario;}

    public void setEstado(String estado) {this.estado=estado;}

    public String getEstado() {return this.estado;}


}
