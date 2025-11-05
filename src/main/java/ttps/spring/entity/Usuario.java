package ttps.spring.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long usuario_id;
    protected String nombre;
    protected String apellido;
    protected String email;
    protected String telefono;
    protected String contrasenia;
    protected boolean condicion, esAdmin;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "barrio_id")
    protected Barrio barrio;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ranking_id")
    protected Ranking ranking;
    @OneToMany(mappedBy = "usuario_puntaje", cascade = CascadeType.ALL)
    protected List<Puntaje> puntajes;
    @OneToMany(mappedBy = "usuario_avistador", cascade = CascadeType.ALL)
    protected List<Avistamiento> avistamientos_publicados;
    @OneToMany(mappedBy = "usuario_publicador", cascade = CascadeType.ALL)
    protected List<Publicacion> publicaciones;

    //este constructor vacio es requerido por JPA
    public Usuario() {}

    public Usuario(String nombre, String apellido, String email, String telefono, String contrasenia, boolean condicion, boolean esAdmin, Barrio barrio, Ranking ranking) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.contrasenia = contrasenia;
        this.condicion = condicion;
        this.esAdmin = esAdmin;
        this.barrio = barrio;
        this.avistamientos_publicados = new ArrayList<>();
        this.publicaciones = new ArrayList<>();
        this.puntajes = new ArrayList<>();
    }


    public void darDeBaja() {}

    public void agregarPuntos(Ranking ranking, int puntos) {
        Puntaje puntajeExistente = puntajes.stream()
                .filter(p -> p.getRanking().equals(ranking))
                .findFirst()
                .orElse(null);

        if (puntajeExistente == null) {
            Puntaje nuevo = new Puntaje(puntos, ranking,this);
            puntajes.add(nuevo);
        } else {
            puntajeExistente.incrementarPuntos(puntos);
        }
    }

    public void registrarPublicacion(Mascota mascota, String estado, LocalDate fecha, LocalDateTime hora, Coordenada coordenada) {
        this.publicaciones.add(new Publicacion(fecha, hora, coordenada, estado, mascota, this));
    }

    public void registrarPublicacion(Publicacion publicacion) {
        this.publicaciones.add(publicacion);
    }

    public void registrarAvistamiento(Mascota mascota, LocalDate fecha, LocalDateTime hora, Coordenada coordenada) {
        this.avistamientos_publicados.add(new Avistamiento(fecha, hora, coordenada, mascota, this));
    }

    public void registrarAvistamiento(Avistamiento avistamiento) {
        this.avistamientos_publicados.add(avistamiento);
    }

    public void incrementarPuntos(int puntos) {
        this.ranking.incrementarPuntos(puntos);
    }

    public void editarPublicacionPropia(Long id, Publicacion publicacion) {
        this.publicaciones.removeIf(p -> id == p.getId());
        this.publicaciones.add(publicacion);
    }

    public void eliminarPublicacionPropia(Long id) {
        this.publicaciones.removeIf(p -> id == p.getId());
    }

    public void deshabilitarCuenta(){
        this.condicion = false;
    }

    public void habilitarCuenta() {
        this.condicion = true;
    }

    public Long getId() {
        return usuario_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public boolean isCondicion() {
        return condicion;
    }

    public void setCondicion(boolean condicion) {
        this.condicion = condicion;
    }

    public boolean isEsAdmin() {
        return esAdmin;
    }

    public void setEsAdmin(boolean esAdmin) {
        this.esAdmin = esAdmin;
    }

    public Barrio getBarrio() {
        return barrio;
    }

    public void setBarrio(Barrio barrio) {
        this.barrio = barrio;
    }

    public Ranking getRanking() {
        return ranking;
    }

    public void setRanking(Ranking ranking) {
        this.ranking = ranking;
    }

    public List<Avistamiento> getAvistamientos_publicados() {
        return avistamientos_publicados;
    }

    public void setAvistamientos_publicados(List<Avistamiento> avistamientos_publicados) {
        this.avistamientos_publicados = avistamientos_publicados;
    }

    public List<Publicacion> getPublicaciones() {
        return publicaciones;
    }

    public void setPublicaciones(List<Publicacion> publicaciones) {
        this.publicaciones = publicaciones;
    }
}