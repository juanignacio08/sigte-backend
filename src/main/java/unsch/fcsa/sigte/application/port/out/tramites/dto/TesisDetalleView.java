package unsch.fcsa.sigte.application.port.out.tramites.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class TesisDetalleView {
    private final UUID id;
    private final Long escuelaId;
    private final String escuelaDenominacion;
    private final String titulo;
    private final String tituloDictaminado;
    private final String estadoCodigo;
    private final String estadoDenominacion;
    private final OffsetDateTime fechaRegistro;
    private final List<AutorView> autores;
    private ExpedienteDetalleView expedienteActual;

    public TesisDetalleView(UUID id, Long escuelaId, String escuelaDenominacion, String titulo, String tituloDictaminado, String estadoCodigo, String estadoDenominacion, OffsetDateTime fechaRegistro, List<AutorView> autores) {
        this.id = id;
        this.escuelaId = escuelaId;
        this.escuelaDenominacion = escuelaDenominacion;
        this.titulo = titulo;
        this.tituloDictaminado = tituloDictaminado;
        this.estadoCodigo = estadoCodigo;
        this.estadoDenominacion = estadoDenominacion;
        this.fechaRegistro = fechaRegistro;
        this.autores = autores;
        expedienteActual = null;
    }

    public UUID getId() {
        return id;
    }

    public Long getEscuelaId() {
        return escuelaId;
    }

    public String getEscuelaDenominacion() {
        return escuelaDenominacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getTituloDictaminado() {
        return tituloDictaminado;
    }

    public String getEstadoCodigo() {
        return estadoCodigo;
    }

    public String getEstadoDenominacion() {
        return estadoDenominacion;
    }

    public OffsetDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public List<AutorView> getAutores() {
        return autores;
    }

    public ExpedienteDetalleView getExpedienteActual() {
        return expedienteActual;
    }

    public void setExpedienteActual(ExpedienteDetalleView expedienteActual) {
        this.expedienteActual = expedienteActual;
    }
}