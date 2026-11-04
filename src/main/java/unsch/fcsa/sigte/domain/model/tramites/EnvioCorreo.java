package unsch.fcsa.sigte.domain.model.tramites;

import unsch.fcsa.sigte.domain.exception.BusinessRuleException;

import java.time.OffsetDateTime;

public class EnvioCorreo {

    private final Long id;
    private Long expedienteId;
    private String asunto;
    private String cuerpo;
    private String destinatariosJson;   // JSON serializado
    private String adjuntosJson;        // JSON serializado
    private OffsetDateTime fechaEnvio;
    private String errorMensaje;
    private Integer estado;
    private Boolean eliminado;

    public EnvioCorreo(Long id, Long expedienteId, String asunto, String cuerpo,
                       String destinatariosJson, String adjuntosJson,
                       OffsetDateTime fechaEnvio, String errorMensaje,
                       Integer estado, Boolean eliminado) {
        this.id = id;
        setExpedienteId(expedienteId);
        setAsunto(asunto);
        this.cuerpo = cuerpo;
        this.destinatariosJson = destinatariosJson;
        this.adjuntosJson = adjuntosJson;
        this.fechaEnvio = fechaEnvio != null ? fechaEnvio : OffsetDateTime.now();
        this.errorMensaje = errorMensaje;
        this.estado = estado != null ? estado : 1;
        this.eliminado = eliminado != null && eliminado;
    }

    // ==================================================================
    // VALIDACIONES
    // ==================================================================

    private void setExpedienteId(Long expedienteId) {
        if (expedienteId == null) {
            throw new BusinessRuleException("El documento de tesis es obligatorio.");
        }
        this.expedienteId = expedienteId;
    }

    private void setAsunto(String asunto) {
        if (asunto == null || asunto.isBlank()) {
            throw new BusinessRuleException("El asunto del correo es obligatorio.");
        }
        this.asunto = asunto.trim();
    }

    // ==================================================================
    // GETTERS
    // ==================================================================

    public Long getId() { return id; }
    public Long getExpedienteId() { return expedienteId; }
    public String getAsunto() { return asunto; }
    public String getCuerpo() { return cuerpo; }
    public String getDestinatariosJson() { return destinatariosJson; }
    public String getAdjuntosJson() { return adjuntosJson; }
    public OffsetDateTime getFechaEnvio() { return fechaEnvio; }
    public String getErrorMensaje() { return errorMensaje; }
    public Integer getEstado() { return estado; }
    public Boolean isEliminado() { return eliminado; }
}