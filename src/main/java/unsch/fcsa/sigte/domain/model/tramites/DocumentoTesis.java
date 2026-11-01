package unsch.fcsa.sigte.domain.model.tramites;

import unsch.fcsa.sigte.domain.exception.BusinessRuleException;
import unsch.fcsa.sigte.domain.util.TablaMaestraCodigos;

import java.time.LocalDate;
import java.util.UUID;

public class DocumentoTesis {
    private final Long id;
    private UUID tesisId;
    private Long expedienteTesisId;
    private String tipoDocumentoCodigo;
    private Integer numeroCorrelativo;
    private Integer anioCorrelativo;
    private LocalDate fechaEmision;
    private String archivoWordUrl;
    private String archivoPdfUrl;
    private Long tamanioBytesWord;
    private Long tamanioBytesPdf;
    private String resultadoCodigo;
    private String observaciones;
    private String estadoCodigo;
    private Integer estado;
    private Boolean eliminado;

    public DocumentoTesis(Long id, UUID tesisId, Long expedienteTesisId,
                          String tipoDocumentoCodigo, Integer numeroCorrelativo,
                          Integer anioCorrelativo, LocalDate fechaEmision,
                          String archivoWordUrl, String archivoPdfUrl, Long tamanioBytesWord,
                          Long tamanioBytesPdf, String resultadoCodigo, String observaciones,
                          String estadoCodigo, Integer estado, Boolean eliminado) {
        this.id = id;
        setTesisId(tesisId);
        this.expedienteTesisId = expedienteTesisId;
        setTipoDocumentoCodigo(tipoDocumentoCodigo);
        setNumeroCorrelativo(numeroCorrelativo);
        this.anioCorrelativo = anioCorrelativo;
        this.fechaEmision = fechaEmision != null ? fechaEmision : LocalDate.now();
        this.archivoWordUrl = archivoWordUrl;
        this.archivoPdfUrl = archivoPdfUrl;
        this.tamanioBytesWord = tamanioBytesWord;
        this.tamanioBytesPdf = tamanioBytesPdf;
        this.resultadoCodigo = resultadoCodigo;
        this.observaciones = observaciones;
        setEstadoCodigo(estadoCodigo);
        this.estado = estado != null ? estado : 1;
        this.eliminado = eliminado != null && eliminado;
    }

    // ==================================================================
    // REGLAS DE NEGOCIO
    // ==================================================================

    public void volverAGenerar(
            String tipoDocumentoCodigo,
            String rutaWord,
            String rutaPdf,
            Long tamanioBytesWord,
            Long tamanioBytesPdf
    ) {
        setTipoDocumentoCodigo(tipoDocumentoCodigo);
        this.archivoWordUrl = rutaWord;
        this.archivoPdfUrl = rutaPdf;
        this.tamanioBytesWord = tamanioBytesWord;
        this.tamanioBytesPdf = tamanioBytesPdf;
        this.fechaEmision = LocalDate.now();
        this.estadoCodigo = TablaMaestraCodigos.ESTADO_DOCUMENTO_ESTADO_GENERADO;
    }

    public void marcarComoEnviado() {
        this.estadoCodigo = TablaMaestraCodigos.ESTADO_DOCUMENTO_ESTADO_ENVIADO;
    }

    public void marcarComoAnulado() {
        this.estadoCodigo = TablaMaestraCodigos.ESTADO_DOCUMENTO_ESTADO_ANULADO;
    }

    public void regenerar(String nuevoWordUrl, String nuevoPdfUrl, Long nuevoTamanioWord, Long nuevoTamanioPdf) {
        this.archivoWordUrl = nuevoWordUrl;
        this.archivoPdfUrl = nuevoPdfUrl;
        this.tamanioBytesWord = nuevoTamanioWord;
        this.tamanioBytesPdf = nuevoTamanioPdf;
        this.fechaEmision = LocalDate.now();
        this.estadoCodigo = TablaMaestraCodigos.ESTADO_DOCUMENTO_ESTADO_GENERADO;
    }

    public void marcarComoEliminado() {
        this.eliminado = true;
        this.estado = 0;
    }

    private void setTesisId(UUID tesisId) {
        if (tesisId == null) {
            throw new BusinessRuleException("La tesis es obligatoria.");
        }
        this.tesisId = tesisId;
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

    private void setNumeroCorrelativo(Integer numeroCorrelativo) {
        if (numeroCorrelativo != null && numeroCorrelativo <= 0) {
            throw new BusinessRuleException("El número correlativo debe ser mayor a cero.");
        }
        this.numeroCorrelativo = numeroCorrelativo;
    }

    private void setEstadoCodigo(String estadoCodigo) {
        if (estadoCodigo == null || estadoCodigo.isBlank()) {
            throw new BusinessRuleException("El estado del documento es obligatorio.");
        }
        if (estadoCodigo.trim().length() != 6) {
            throw new BusinessRuleException("El código de estado debe tener 6 caracteres.");
        }
        this.estadoCodigo = estadoCodigo.trim();
    }

    public Long getId() { return id; }
    public UUID getTesisId() { return tesisId; }
    public Long getExpedienteTesisId() { return expedienteTesisId; }
    public String getTipoDocumentoCodigo() { return tipoDocumentoCodigo; }
    public Integer getNumeroCorrelativo() { return numeroCorrelativo; }
    public Integer getAnioCorrelativo() { return anioCorrelativo; }
    public LocalDate getFechaEmision() { return fechaEmision; }
    public String getArchivoWordUrl() { return archivoWordUrl; }
    public String getArchivoPdfUrl() { return archivoPdfUrl; }
    public Long getTamanioBytesWord() { return tamanioBytesWord; }
    public Long getTamanioBytesPdf() { return tamanioBytesPdf; }
    public String getResultadoCodigo() { return resultadoCodigo; }
    public String getObservaciones() { return observaciones; }
    public String getEstadoCodigo() { return estadoCodigo; }
    public Integer getEstado() { return estado; }
    public Boolean isEliminado() { return eliminado; }
}
