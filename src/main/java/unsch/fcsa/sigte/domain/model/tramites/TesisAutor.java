package unsch.fcsa.sigte.domain.model.tramites;

import unsch.fcsa.sigte.domain.exception.BusinessRuleException;

import java.time.OffsetDateTime;
import java.util.UUID;

public class TesisAutor {

    private Long id;
    private UUID tesisId;
    private Long estudianteId;
    private OffsetDateTime fechaAsociacion;
    private Integer estado;
    private Boolean eliminado;

    public TesisAutor(Long id, UUID tesisId, Long estudianteId, OffsetDateTime fechaAsociacion, Integer estado, Boolean eliminado) {
        this.id = id;
        this.tesisId = tesisId;
        setEstudianteId(estudianteId);
        this.fechaAsociacion = fechaAsociacion != null ? fechaAsociacion : OffsetDateTime.now();
        this.estado = estado != null ? estado : 1;
        this.eliminado = eliminado != null && eliminado;
    }

    private void setEstudianteId(Long estudianteId) {
        if (estudianteId == null) {
            throw new BusinessRuleException("El ID del estudiante autor es obligatorio.");
        }
        this.estudianteId = estudianteId;
    }

    public Long getId() { return id; }
    public UUID getTesisId() { return tesisId; }
    public Long getEstudianteId() { return estudianteId; }
    public OffsetDateTime getFechaAsociacion() { return fechaAsociacion; }
    public Integer getEstado() { return estado; }
    public Boolean isEliminado() { return eliminado; }
}