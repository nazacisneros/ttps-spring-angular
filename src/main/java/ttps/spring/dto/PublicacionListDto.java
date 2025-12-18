package ttps.spring.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PublicacionListDto {
    private Long id;
    private LocalDate fecha;
    private LocalDateTime hora;
    private String estado;
    private String nombreMascota;
    private String tamanioMascota;
    private String colorMascota;
    private String descripcionMascota;
    private String estadoMascota;
    private String latitud;
    private String longitud;

    public PublicacionListDto(Long id, LocalDate fecha, LocalDateTime hora, String estado,
            String nombreMascota, String tamanioMascota, String colorMascota,
            String descripcionMascota, String estadoMascota, String latitud, String longitud) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.nombreMascota = nombreMascota;
        this.tamanioMascota = tamanioMascota;
        this.colorMascota = colorMascota;
        this.descripcionMascota = descripcionMascota;
        this.estadoMascota = estadoMascota;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombreMascota() {
        return nombreMascota;
    }

    public void setNombreMascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }

    public String getTamanioMascota() {
        return tamanioMascota;
    }

    public void setTamanioMascota(String tamanioMascota) {
        this.tamanioMascota = tamanioMascota;
    }

    public String getColorMascota() {
        return colorMascota;
    }

    public void setColorMascota(String colorMascota) {
        this.colorMascota = colorMascota;
    }

    public String getDescripcionMascota() {
        return descripcionMascota;
    }

    public void setDescripcionMascota(String descripcionMascota) {
        this.descripcionMascota = descripcionMascota;
    }

    public String getEstadoMascota() {
        return estadoMascota;
    }

    public void setEstadoMascota(String estadoMascota) {
        this.estadoMascota = estadoMascota;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
