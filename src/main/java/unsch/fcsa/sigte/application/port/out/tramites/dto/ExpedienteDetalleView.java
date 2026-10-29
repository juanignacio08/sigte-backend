package unsch.fcsa.sigte.application.port.out.tramites.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class ExpedienteDetalleView {
    private final Long id;
    private final UUID tesisId;
    private final String numeroExpediente;
    private final String etapaCodigo;
    private final String etapaDenominacion;
    private final Integer numeroRevision;
    private final OffsetDateTime fechaIngreso;
    private final String estadoCodigo;
    private final String estadoDenominacion;
    private final Integer ultimoPaso;
    private final Long comisionId;
    private final Integer estado;
    private List<ExpedienteArchivoDetalleView> archivos;

    public ExpedienteDetalleView(Long id, UUID tesisId, String numeroExpediente,
                                 String etapaCodigo, String etapaDenominacion,
                                 Integer numeroRevision, OffsetDateTime fechaIngreso,
                                 String estadoCodigo, String estadoDenominacion,
                                 Integer ultimoPaso, Long comisionId, Integer estado
                                 ) {
        this.id = id;
        this.tesisId = tesisId;
        this.numeroExpediente = numeroExpediente;
        this.etapaCodigo = etapaCodigo;
        this.etapaDenominacion = etapaDenominacion;
        this.numeroRevision = numeroRevision;
        this.fechaIngreso = fechaIngreso;
        this.estadoCodigo = estadoCodigo;
        this.estadoDenominacion = estadoDenominacion;
        this.ultimoPaso = ultimoPaso;
        this.comisionId = comisionId;
        this.estado = estado;
        this.archivos = null;
    }

    public Long getId() {
        return id;
    }

    public UUID getTesisId() {
        return tesisId;
    }

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public String getEtapaCodigo() {
        return etapaCodigo;
    }

    public String getEtapaDenominacion() {
        return etapaDenominacion;
    }

    public Integer getNumeroRevision() {
        return numeroRevision;
    }

    public OffsetDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public String getEstadoCodigo() { return estadoCodigo; }

    public String getEstadoDenominacion() { return estadoDenominacion; }

    public Integer getUltimoPaso() { return ultimoPaso; }

    public Long getComisionId() { return comisionId; }

    public Integer getEstado() {
        return estado;
    }

    public List<ExpedienteArchivoDetalleView> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<ExpedienteArchivoDetalleView> archivos) {
        this.archivos = archivos;
    }
}
