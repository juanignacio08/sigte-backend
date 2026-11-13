package unsch.fcsa.sigte.domain.model.tramites;

import unsch.fcsa.sigte.domain.exception.BusinessRuleException;

import java.time.LocalDate;
import java.util.UUID;

public class Comision {
    private final Long id;
    private UUID tesisId;
    private String etapaCodigo;
    private LocalDate fechaConformacion;
    private Integer estado;
    private Boolean eliminado;

    public Comision(Long id, UUID tesisId, String etapaCodigo, LocalDate fechaConformacion,
                    Integer estado, Boolean eliminado) {
        this.id = id;
        setTesisId(tesisId);
        setEtapaCodigo(etapaCodigo);
        this.fechaConformacion = fechaConformacion != null ? fechaConformacion : LocalDate.now();
        this.estado = estado != null ? estado : 1;
        this.eliminado = eliminado != null && eliminado;
    }

    public void actualizarDatos(String etapaCodigo, LocalDate fechaConformacion) {
        setEtapaCodigo(etapaCodigo);
        if (fechaConformacion != null) {
            this.fechaConformacion = fechaConformacion;
        }
    }

    public void activar() { this.estado = 1; }

    public void desactivar() { this.estado = 0; }

    public void marcarComoEliminado() {
        this.eliminado = true;
        this.estado = 0;
    }

    private void setTesisId(UUID tesisId) {
        if (tesisId == null) {
            throw new BusinessRuleException("La tesis es obligatoria para conformar una comisión.");
        }
        this.tesisId = tesisId;
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

    public Long getId() { return id; }
    public UUID getTesisId() { return tesisId; }
    public String getEtapaCodigo() { return etapaCodigo; }
    public LocalDate getFechaConformacion() { return fechaConformacion; }
    public Integer getEstado() { return estado; }
    public Boolean isEliminado() { return eliminado; }
}
