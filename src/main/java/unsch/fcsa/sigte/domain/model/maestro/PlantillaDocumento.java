package unsch.fcsa.sigte.domain.model.maestro;

import unsch.fcsa.sigte.domain.exception.BusinessRuleException;
import unsch.fcsa.sigte.domain.util.PathSanitizer;

public class PlantillaDocumento {

    private final Long id;
    private Long escuelaId;
    private String tipoDocumentoCodigo;
    private String nombreDocumento;
    private String archivoUrl;
    private Integer plazoDias;
    private Integer plazoMeses;
    private Integer estado;
    private Boolean eliminado;

    public PlantillaDocumento(Long id, Long escuelaId, String tipoDocumentoCodigo,
                              String nombreDocumento, String archivoUrl, Integer plazoDias, Integer plazoMeses,
                              Integer estado, Boolean eliminado) {
        this.id = id;
        setEscuelaId(escuelaId);
        setTipoDocumentoCodigo(tipoDocumentoCodigo);
        setNombreDocumento(nombreDocumento);
        setArchivoUrl(archivoUrl);
        setPlazoDias(plazoDias);
        setPlazoMeses(plazoMeses);
        this.estado = estado != null ? estado : 1;
        this.eliminado = eliminado != null && eliminado;
    }

    public boolean tienePlazo() {
        return plazoDias != null || plazoMeses != null;
    }

    public boolean esPlazoEnDias() {
        return plazoDias != null;
    }

    public boolean esPlazoEnMeses() {
        return plazoMeses != null;
    }

    public void actualizarArchivo(String nuevoArchivoUrl) {
        setArchivoUrl(nuevoArchivoUrl);
    }

    public void actualizarPlazos(Integer nuevoPlazoDias, Integer nuevoPlazoMeses) {
        setPlazoDias(nuevoPlazoDias);
        setPlazoMeses(nuevoPlazoMeses);
    }

    public void desactivar() {
        this.estado = 0;
    }

    public void activar() {
        this.estado = 1;
    }

    public void marcarComoEliminado() {
        this.eliminado = true;
        this.estado = 0;
    }

    private void setEscuelaId(Long escuelaId) {
        if (escuelaId == null) {
            throw new BusinessRuleException("La escuela es obligatoria.");
        }
        this.escuelaId = escuelaId;
    }

    private void setTipoDocumentoCodigo(String tipoDocumentoCodigo) {
        if (tipoDocumentoCodigo == null || tipoDocumentoCodigo.isBlank()) {
            throw new BusinessRuleException("El tipo de documento es obligatorio.");
        }
        if (tipoDocumentoCodigo.trim().length() != 6) {
            throw new BusinessRuleException("El código de tipo debe tener 6 caracteres.");
        }
        this.tipoDocumentoCodigo = tipoDocumentoCodigo.trim();
    }

    private void setNombreDocumento(String nombreDocumento) {
        if (nombreDocumento == null || nombreDocumento.isBlank()) {
            throw new BusinessRuleException("El nombre de documento es obligatorio.");
        }
        this.nombreDocumento = PathSanitizer.sanitizeRelativePath(nombreDocumento.trim());
    }

    private void setArchivoUrl(String archivoUrl) {
        if (archivoUrl == null || archivoUrl.isBlank()) {
            throw new BusinessRuleException("La URL de la plantilla es obligatoria.");
        }
        this.archivoUrl = archivoUrl.trim();
    }

    private void setPlazoDias(Integer plazoDias) {
        if (plazoDias != null && plazoDias <= 0) {
            throw new BusinessRuleException("El plazo en días debe ser mayor a cero.");
        }
        this.plazoDias = plazoDias;
    }

    private void setPlazoMeses(Integer plazoMeses) {
        if (plazoMeses != null && plazoMeses <= 0) {
            throw new BusinessRuleException("El plazo en meses debe ser mayor a cero.");
        }
        this.plazoMeses = plazoMeses;
    }

    public Long getId() { return id; }
    public Long getEscuelaId() { return escuelaId; }
    public String getTipoDocumentoCodigo() { return tipoDocumentoCodigo; }
    public String getNombreDocumento() { return nombreDocumento; }
    public String getArchivoUrl() { return archivoUrl; }
    public Integer getPlazoDias() { return plazoDias; }
    public Integer getPlazoMeses() { return plazoMeses; }
    public Integer getEstado() { return estado; }
    public Boolean isEliminado() { return eliminado; }
}