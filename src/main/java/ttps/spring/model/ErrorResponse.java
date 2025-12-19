package ttps.spring.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class ErrorResponse {
    @JsonProperty("codigo")
    private int codigo;

    @JsonProperty("mensaje")
    private String mensaje;

    @JsonProperty("timestamp")
    private long timestamp;

    @JsonProperty("timestampDate")
    private LocalDateTime timestampDate;

    @JsonProperty("path")
    private String path;

    public ErrorResponse(int codigo, String mensaje, long timestamp) {
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.timestamp = timestamp;
    }

    public ErrorResponse(int codigo, String mensaje, LocalDateTime timestampDate, String path) {
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.timestampDate = timestampDate;
        this.timestamp = System.currentTimeMillis();
        this.path = path;
    }

    public ErrorResponse() {
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDateTime getTimestampDate() {
        return timestampDate;
    }

    public void setTimestampDate(LocalDateTime timestampDate) {
        this.timestampDate = timestampDate;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}