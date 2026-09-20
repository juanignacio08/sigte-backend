package unsch.fcsa.sigte.domain.model.maestro;

import unsch.fcsa.sigte.domain.util.PathSanitizer;

public class EscuelaProfesional {
    private Long id;
    private Long departamentoAcademicoId;
    private Integer codigo;
    private String abreviatura;
    private String denominacion;
    private String plantillaMemorando;
    private String plantillaResolucion;
    private Integer estado;
    private Boolean eliminado;

    public EscuelaProfesional(
            Long departamentoAcademicoId,
            Integer codigo,
            String abreviatura,
            String denominacion,
            String plantillaMemorando,
            String plantillaResolucion
    ) {
        setDepartamentoAcademicoId(departamentoAcademicoId);
        setCodigo(codigo);
        setAbreviatura(abreviatura);
        setDenominacion(denominacion);
        setPlantillaMemorando(plantillaMemorando);
        setPlantillaResolucion(plantillaResolucion);
        estado = 1;
        eliminado = false;
    }

    public EscuelaProfesional(
            Long id,
            Long departamentoAcademicoId,
            Integer codigo,
            String abreviatura,
            String denominacion,
            String plantillaMemorando,
            String plantillaResolucion,
            Integer estado,
            Boolean eliminado
    ) {
        this.id = id;
        setDepartamentoAcademicoId(departamentoAcademicoId);
        setCodigo(codigo);
        setAbreviatura(abreviatura);
        setDenominacion(denominacion);
        setPlantillaMemorando(plantillaMemorando);
        setPlantillaResolucion(plantillaResolucion);
        this.estado = estado != null ? estado : 1;
        this.eliminado = eliminado != null && eliminado;
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

    public void actualizarDatos(
            Long departamentoAcademicoId,
            Integer codigo,
            String abreviatura,
            String denominacion,
            String plantillaMemorando,
            String plantillaResolucion
    ) {
        setDepartamentoAcademicoId(departamentoAcademicoId);
        setCodigo(codigo);
        setAbreviatura(abreviatura);
        setDenominacion(denominacion);
        setPlantillaMemorando(plantillaMemorando);
        setPlantillaResolucion(plantillaResolucion);
    }

    private void setDepartamentoAcademicoId(Long departamentoAcademicoId) {
        if (departamentoAcademicoId == null || departamentoAcademicoId <= 0) {
            throw new IllegalArgumentException("El departamento académico no puede estar vacío.");
        }

        this.departamentoAcademicoId = departamentoAcademicoId;
    }

    private void setCodigo(Integer codigo) {
        if (codigo == null || codigo <= 0) {
            throw new IllegalArgumentException("El código no puede ser nulo o negativo");
        }

        this.codigo = codigo;
    }

    private void setAbreviatura(String abreviatura) {
        if (abreviatura == null || abreviatura.trim().isEmpty()) {
            throw new IllegalArgumentException("La abreviatura no puede estar vacía.");
        }
        if (abreviatura.trim().length() > 10) {
            throw new IllegalArgumentException("La abreviatura no puede superar los 10 caracteres.");
        }
        this.abreviatura = abreviatura.trim().toUpperCase();
    }

    private void setDenominacion(String denominacion) {
        if (denominacion == null || denominacion.trim().isEmpty()) {
            throw new IllegalArgumentException("La denominación no puede estar vacía.");
        }

        if (denominacion.length() > 200) {
            throw new IllegalArgumentException("La denominación no puede superar los 200 caracteres.");
        }

        this.denominacion = denominacion.trim();
    }

    private void setPlantillaMemorando(String plantillaMemorando) {
        if (plantillaMemorando != null && !plantillaMemorando.trim().isEmpty()) {
            this.plantillaMemorando = PathSanitizer.sanitizeRelativePath(plantillaMemorando.trim());
        }
    }

    private void setPlantillaResolucion(String plantillaResolucion) {
        if (plantillaResolucion != null && !plantillaResolucion.trim().isEmpty()) {
            this.plantillaResolucion = PathSanitizer.sanitizeRelativePath(plantillaResolucion.trim());
        }
    }

    public Long getId() { return id; }
    public Long getDepartamentoAcademicoId() { return departamentoAcademicoId; }
    public Integer getCodigo() { return codigo; }
    public String getAbreviatura() { return abreviatura; }
    public String getDenominacion() { return denominacion; }
    public String getPlantillaMemorando() { return plantillaMemorando; }
    public String getPlantillaResolucion() { return plantillaResolucion; }
    public Integer getEstado() { return estado; }
    public Boolean isEliminado() { return eliminado; }
}
