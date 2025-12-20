package ttps.spring.dto;

public class CrearPublicacionRequest {
    private MascotaData mascota;
    private Long barrioId;
    private CoordenadaData coordenadas;
    private String fecha;
    private String hora;
    private String estado;

    public CrearPublicacionRequest() {
    }

    public MascotaData getMascota() {
        return mascota;
    }

    public void setMascota(MascotaData mascota) {
        this.mascota = mascota;
    }

    public Long getBarrioId() {
        return barrioId;
    }

    public void setBarrioId(Long barrioId) {
        this.barrioId = barrioId;
    }

    public CoordenadaData getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(CoordenadaData coordenadas) {
        this.coordenadas = coordenadas;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
