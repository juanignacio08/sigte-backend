package unsch.fcsa.sigte.application.port.out.tramites.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class ComisionDetalleView {
    private final Long id;
    private final UUID tesisId;
    private final String etapaCodigo;
    private final String etapaDenominacion;
    private final LocalDate fechaConformacion;
    private final Integer estado;
    private List<ComisionMiembroDetalleView> miembros;

    public ComisionDetalleView(Long id, UUID tesisId, String etapaCodigo, String etapaDenominacion,
                               LocalDate fechaConformacion, Integer estado) {
        this.id = id;
        this.tesisId = tesisId;
        this.etapaCodigo = etapaCodigo;
        this.etapaDenominacion = etapaDenominacion;
        this.fechaConformacion = fechaConformacion;
        this.estado = estado;
        this.miembros = null;
    }

    public Long getId() { return id; }
    public UUID getTesisId() { return tesisId; }
    public String getEtapaCodigo() { return etapaCodigo; }
    public String getEtapaDenominacion() { return etapaDenominacion; }
    public LocalDate getFechaConformacion() { return fechaConformacion; }
    public Integer getEstado() { return estado; }
    public List<ComisionMiembroDetalleView> getMiembros() { return miembros; }
    public void setMiembros(List<ComisionMiembroDetalleView> miembros) {
        this.miembros = miembros;
    }
}
