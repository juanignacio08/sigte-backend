package unsch.fcsa.sigte.domain.model.tramites;

import unsch.fcsa.sigte.domain.exception.BusinessRuleException;

import java.time.LocalDate;

public class ComisionMiembro {

    private final Long id;
    private Long comisionId;
    private Long docenteId;
    private String rolCodigo;
    private Integer orden;
    private LocalDate fechaAlta;
    private LocalDate fechaBaja;
    private String motivoBaja;
    private Integer estado;
    private Boolean eliminado;

    public ComisionMiembro(Long id, Long comisionId, Long docenteId, String rolCodigo,
                           Integer orden, LocalDate fechaAlta, LocalDate fechaBaja, String motivoBaja,
                           Integer estado, Boolean eliminado) {
        this.id = id;
        setComisionId(comisionId);
        setDocenteId(docenteId);
        setRolCodigo(rolCodigo);
        this.orden = orden != null ? orden : 1;
        this.fechaAlta = fechaAlta != null ? fechaAlta : LocalDate.now();
        this.fechaBaja = fechaBaja;
        this.motivoBaja = motivoBaja;
        this.estado = estado != null ? estado : 1;
        this.eliminado = eliminado != null && eliminado;
    }

    public void darDeBaja(String motivo) {
        this.fechaBaja = LocalDate.now();
        this.motivoBaja = motivo;
        this.estado = 0;
        this.eliminado = true;
    }

    public void actualizarRol(String rol, int orden) {
        setRolCodigo(rol);
        this.orden = orden;
    }

    private void setComisionId(Long comisionId) {
        if (comisionId == null) throw new BusinessRuleException("La comisión es obligatoria.");
        this.comisionId = comisionId;
    }

    private void setDocenteId(Long docenteId) {
        if (docenteId == null) throw new BusinessRuleException("El docente es obligatorio.");
        this.docenteId = docenteId;
    }

    private void setRolCodigo(String rolCodigo) {
        if (rolCodigo == null || rolCodigo.isBlank()) {
            throw new BusinessRuleException("El rol del miembro es obligatorio.");
        }
        if (rolCodigo.trim().length() != 6) {
            throw new BusinessRuleException("El código de rol debe tener exactamente 6 caracteres.");
        }
        this.rolCodigo = rolCodigo.trim();
    }

    public Long getId() { return id; }
    public Long getComisionId() { return comisionId; }
    public Long getDocenteId() { return docenteId; }
    public String getRolCodigo() { return rolCodigo; }
    public Integer getOrden() { return orden; }
    public LocalDate getFechaAlta() { return fechaAlta; }
    public LocalDate getFechaBaja() { return fechaBaja; }
    public String getMotivoBaja() { return motivoBaja; }
    public Integer getEstado() { return estado; }
    public Boolean isEliminado() { return eliminado; }
}
