package unsch.fcsa.sigte.domain.model.tramites;

import unsch.fcsa.sigte.domain.exception.BusinessRuleException;
import unsch.fcsa.sigte.domain.util.PathSanitizer;

public class ExpedienteArchivo {
    private Long id;
    private Long expedienteTesisId;
    private String nombreArchivo;
    private String archivoUrl;
    private Long tamanioBytes;
    private Integer estado;
    private boolean eliminado;

    public ExpedienteArchivo(Long id, Long expedienteTesisId, String nombreArchivo,
                             String archivoUrl, Long tamanioBytes, Integer estado, Boolean eliminado) {
        this.id = id;
        this.expedienteTesisId = expedienteTesisId;
        setNombreArchivo(nombreArchivo);
        setArchivoUrl(archivoUrl);
        this.tamanioBytes = tamanioBytes;
        this.estado = estado != null ? estado : 1;
        this.eliminado = eliminado != null && eliminado;
    }

    private void setNombreArchivo(String nombreArchivo) {
        if (nombreArchivo == null || nombreArchivo.isBlank()) {
            throw new BusinessRuleException("El nombre del archivo es obligatorio.");
        }

        if (nombreArchivo.trim().length() > 255) {
            nombreArchivo = nombreArchivo.substring(0, 255);
        }

        this.nombreArchivo = nombreArchivo.trim();
    }

    private void setArchivoUrl(String archivoUrl) {
        if (archivoUrl == null || archivoUrl.trim().isEmpty()) {
            throw new BusinessRuleException("El url del archivo es obligatorio.");
        }

        this.archivoUrl = PathSanitizer.sanitizeRelativePath(archivoUrl.trim());
    }

    public Long getId() { return id; }
    public Long getExpedienteTesisId() { return expedienteTesisId; }
    public String getNombreArchivo() { return nombreArchivo; }
    public String getArchivoUrl() { return archivoUrl; }
    public Long getTamanioBytes() { return tamanioBytes; }
    public Integer getEstado() { return estado; }
    public Boolean isEliminado() { return eliminado; }
}
