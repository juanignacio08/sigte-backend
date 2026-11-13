package unsch.fcsa.sigte.application.port.out.tramites.dto;

import java.util.List;

public class ContenidoEmailMemo {
    private Long documentoId;
    private List<DestinatarioEmail> destinatarios;
    private String asunto;
    private String cuerpo;
    private List<ExpedienteArchivoDetalleView> archivos;

    public ContenidoEmailMemo(Long documentoId, List<DestinatarioEmail> destinatarios, String asunto, String cuerpo, List<ExpedienteArchivoDetalleView> archivos) {
        this.documentoId = documentoId;
        this.destinatarios = destinatarios;
        this.asunto = asunto;
        this.cuerpo = cuerpo;
        this.archivos = archivos;
    }

    public ContenidoEmailMemo() {
    }

    public Long getDocumentoId() {
        return documentoId;
    }

    public void setDocumentoId(Long documentoId) {
        this.documentoId = documentoId;
    }

    public List<DestinatarioEmail> getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(List<DestinatarioEmail> destinatarios) {
        this.destinatarios = destinatarios;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public List<ExpedienteArchivoDetalleView> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<ExpedienteArchivoDetalleView> archivos) {
        this.archivos = archivos;
    }
}
