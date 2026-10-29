package unsch.fcsa.sigte.domain.model.tramites;

import unsch.fcsa.sigte.domain.exception.BusinessRuleException;

import java.time.OffsetDateTime;

public class ExpedienteTesis {

    private final Long id;
    private java.util.UUID tesisId;
    private String numeroExpediente;
    private String etapaCodigo;
    private Integer numeroRevision;
    private OffsetDateTime fechaIngreso;
    private String estadoCodigo;
    private Integer ultimoPaso;
    private Long comisionId;
    private String motivoBaja;
    private Integer estado;
    private Boolean eliminado;

    public ExpedienteTesis(Long id, java.util.UUID tesisId, String numeroExpediente, String etapaCodigo,
                           Integer numeroRevision, OffsetDateTime fechaIngreso, String estadoCodigo, Integer ultimoPaso,
                           Long comisionId, Integer estado, Boolean eliminado, String motivoBaja) {
        this.id = id;
        this.tesisId = tesisId;
        setNumeroExpediente(numeroExpediente);
        setEtapaCodigo(etapaCodigo);
        this.numeroRevision = numeroRevision != null ? numeroRevision : 1;
        this.fechaIngreso = fechaIngreso != null ? fechaIngreso : OffsetDateTime.now();
        setEstadoCodigo(estadoCodigo);
        this.ultimoPaso = ultimoPaso != null ? ultimoPaso : 1;
        this.comisionId = comisionId;
        this.estado = estado != null ? estado : 1;
        this.eliminado = eliminado != null && eliminado;
        this.motivoBaja = motivoBaja;
    }

    public void actualizarDatos(
            String numeroExpediente,
            Integer numeroRevision,
            String etapaCodigo,
            OffsetDateTime fechaIngreso,
            Integer ultimoPaso,
            String estadoCodigo
    ) {
        setNumeroExpediente(numeroExpediente);
        this.numeroRevision = numeroRevision != null ? numeroRevision : 1;
        setEtapaCodigo(etapaCodigo);
        if (fechaIngreso != null) {
            this.fechaIngreso = fechaIngreso;
        }
        this.ultimoPaso = ultimoPaso != null ? ultimoPaso : 1;
        setEstadoCodigo(estadoCodigo);
    }

    public void activar() {
        this.motivoBaja = "Se volvio a activar";
        this.estado = 1;
    }

    public void desactivar(String motivoBaja) {
        this.motivoBaja = motivoBaja;
        this.estado = 0;
    }

    public void marcarComoEliminado() {
        this.eliminado = true;
        this.estado = 0;
    }

    private void setNumeroExpediente(String numeroExpediente) {
        if (numeroExpediente == null || numeroExpediente.isBlank()) {
            throw new BusinessRuleException("El número de expediente es obligatorio.");
        }

        if (numeroExpediente.trim().length() > 50) {
            throw new BusinessRuleException("El número de expediente no puede superar los 50 caracteres.");
        }

        this.numeroExpediente = numeroExpediente.trim();
    }

    private void setEtapaCodigo(String etapaCodigo) {
        if (etapaCodigo == null || etapaCodigo.isBlank()) {
            throw new BusinessRuleException("El código de etapa es obligatorio.");
        }
        if (etapaCodigo.trim().length() != 6) {
            throw new BusinessRuleException("El código de etapa debe tener exactamente 6 caracteres.");
        }
        this.etapaCodigo = etapaCodigo.trim();
    }

    private void setEstadoCodigo(String estadoCodigo) {
        if (estadoCodigo == null || estadoCodigo.isBlank()) {
            throw new BusinessRuleException("El código del estado es obligatorio.");
        }
        if (estadoCodigo.trim().length() != 6) {
            throw new BusinessRuleException("El código de etapa debe tener exactamente 6 caracteres.");
        }
        this.estadoCodigo = estadoCodigo.trim();
    }

    public Long getId() { return id; }
    public java.util.UUID getTesisId() { return tesisId; }
    public String getNumeroExpediente() { return numeroExpediente; }
    public String getEtapaCodigo() { return etapaCodigo; }
    public Integer getNumeroRevision() { return numeroRevision; }
    public OffsetDateTime getFechaIngreso() { return fechaIngreso; }
    public String getEstadoCodigo() { return estadoCodigo; }
    public Integer getUltimoPaso() { return ultimoPaso; }
    public Long getComisionId() { return comisionId; }
    public String getMotivoBaja() { return motivoBaja; }
    public Integer getEstado() { return estado; }
    public Boolean isEliminado() { return eliminado; }

}