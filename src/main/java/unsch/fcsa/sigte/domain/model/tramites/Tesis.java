package unsch.fcsa.sigte.domain.model.tramites;

import unsch.fcsa.sigte.domain.exception.BusinessRuleException;
import unsch.fcsa.sigte.domain.util.TablaMaestraCodigos;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tesis {

    private final UUID id;
    private Long escuelaId;
    private String titulo;
    private String tituloDictaminado;
    private String estadoCodigo;
    private OffsetDateTime fechaRegistro;
    private Integer estado;
    private Boolean eliminado;

    private List<TesisAutor> autores;

    public Tesis(UUID id, Long escuelaId, String titulo, String tituloDictaminado, String estadoCodigo,
                 OffsetDateTime fechaRegistro, Integer estado, Boolean eliminado) {
        this.id = id;
        setEscuelaId(escuelaId);
        setTitulo(titulo);
        this.tituloDictaminado = tituloDictaminado != null ? tituloDictaminado.trim() : null;
        setEstadoCodigo(estadoCodigo);
        this.fechaRegistro = fechaRegistro != null ? fechaRegistro : OffsetDateTime.now();
        this.estado = estado != null ? estado : 1;
        this.eliminado = eliminado != null && eliminado;
        this.autores = new ArrayList<>();
    }

    public void verificarEditable() {
        // Solo se permite si estadoCodigo es 003001 (EN TRAMITE) o 003003 (OBSERVADO)
        if (!TablaMaestraCodigos.TESIS_ESTADO_EN_TRAMITE.equals(this.estadoCodigo) && !TablaMaestraCodigos.TESIS_ESTADO_OBSERVADO.equals(this.estadoCodigo)) {
            throw new BusinessRuleException("La tesis no se encuentra en un estado editable.");
        }
    }

    public void actualizarDatos(Long escuelaId, String titulo) {
        verificarEditable();
        setEscuelaId(escuelaId);
        setTitulo(titulo);
    }

    private void setEscuelaId(Long escuelaId) {
        if (escuelaId == null) {
            throw new BusinessRuleException("El ID de la escuela es obligatorio.");
        }
        this.escuelaId = escuelaId;
    }

    private void setTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new BusinessRuleException("El título de la tesis es obligatorio.");
        }
        this.titulo = titulo.trim();
    }

    private void setEstadoCodigo(String estadoCodigo) {
        if (estadoCodigo == null || estadoCodigo.isBlank()) {
            throw new BusinessRuleException("El código de estado de tesis es obligatorio.");
        }
        if (estadoCodigo.trim().length() != 6) {
            throw new BusinessRuleException("El código de estado debe tener 6 caracteres.");
        }
        this.estadoCodigo = estadoCodigo.trim();
    }

    public UUID getId() { return id; }
    public Long getEscuelaId() { return escuelaId; }
    public String getTitulo() { return titulo; }
    public String getTituloDictaminado() { return tituloDictaminado; }
    public String getEstadoCodigo() { return estadoCodigo; }
    public OffsetDateTime getFechaRegistro() { return fechaRegistro; }
    public Integer getEstado() { return estado; }
    public Boolean isEliminado() { return eliminado; }
    public List<TesisAutor> getAutores() { return autores; }

    public void setAutores(List<TesisAutor> autores) { this.autores = autores; }

}