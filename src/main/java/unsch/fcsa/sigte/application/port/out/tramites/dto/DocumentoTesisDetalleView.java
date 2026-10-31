package unsch.fcsa.sigte.application.port.out.tramites.dto;

import java.time.LocalDate;

public class DocumentoTesisDetalleView {

    private final Long id;
    private final Long expedienteTesisId;
    private final String tipoDocumentoCodigo;
    private final String tipoDocumentoDenominacion;
    private final Integer numeroCorrelativo;
    private final Integer anioCorrelativo;
    private final LocalDate fechaEmision;
    private final String archivoWordUrl;
    private final String archivoPdfUrl;
    private final Long tamanioBytesWord;
    private final Long tamanioBytesPdf;
    private final String resultadoCodigo;
    private final String resultadoDenominacion;
    private final String observaciones;
    private final String estadoCodigo;
    private final String estadoDenominacion;

    public DocumentoTesisDetalleView(Long id, Long expedienteTesisId,
                                     String tipoDocumentoCodigo, String tipoDocumentoDenominacion,
                                     Integer numeroCorrelativo, Integer anioCorrelativo,
                                     LocalDate fechaEmision,
                                     String archivoWordUrl, String archivoPdfUrl,
                                     Long tamanioBytesWord, Long tamanioBytesPdf,
                                     String resultadoCodigo, String resultadoDenominacion,
                                     String observaciones,
                                     String estadoCodigo, String estadoDenominacion) {
        this.id = id;
        this.expedienteTesisId = expedienteTesisId;
        this.tipoDocumentoCodigo = tipoDocumentoCodigo;
        this.tipoDocumentoDenominacion = tipoDocumentoDenominacion;
        this.numeroCorrelativo = numeroCorrelativo;
        this.anioCorrelativo = anioCorrelativo;
        this.fechaEmision = fechaEmision;
        this.archivoWordUrl = archivoWordUrl;
        this.archivoPdfUrl = archivoPdfUrl;
        this.tamanioBytesWord = tamanioBytesWord;
        this.tamanioBytesPdf = tamanioBytesPdf;
        this.resultadoCodigo = resultadoCodigo;
        this.resultadoDenominacion = resultadoDenominacion;
        this.observaciones = observaciones;
        this.estadoCodigo = estadoCodigo;
        this.estadoDenominacion = estadoDenominacion;
    }

    public Long getId() { return id; }
    public Long getExpedienteTesisId() { return expedienteTesisId; }
    public String getTipoDocumentoCodigo() { return tipoDocumentoCodigo; }
    public String getTipoDocumentoDenominacion() { return tipoDocumentoDenominacion; }
    public Integer getNumeroCorrelativo() { return numeroCorrelativo; }
    public Integer getAnioCorrelativo() { return anioCorrelativo; }
    public LocalDate getFechaEmision() { return fechaEmision; }
    public String getArchivoWordUrl() { return archivoWordUrl; }
    public String getArchivoPdfUrl() { return archivoPdfUrl; }
    public Long getTamanioBytesWord() { return tamanioBytesWord; }
    public Long getTamanioBytesPdf() { return tamanioBytesPdf; }
    public String getResultadoCodigo() { return resultadoCodigo; }
    public String getResultadoDenominacion() { return resultadoDenominacion; }
    public String getObservaciones() { return observaciones; }
    public String getEstadoCodigo() { return estadoCodigo; }
    public String getEstadoDenominacion() { return estadoDenominacion; }
}