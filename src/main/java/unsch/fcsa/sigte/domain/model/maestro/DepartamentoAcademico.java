package unsch.fcsa.sigte.domain.model.maestro;

public class DepartamentoAcademico {

    private Long id;
    private Long facultadId;
    private String abreviatura;
    private String denominacion;
    private Integer estado;
    private Boolean eliminado;

    // Constructor 1: Para crear una nueva instancia desde la capa de aplicación/caso de uso
    public DepartamentoAcademico(Long facultadId, String abreviatura, String denominacion) {
        setFacultadId(facultadId);
        setAbreviatura(abreviatura);
        setDenominacion(denominacion);
        this.estado = 1;
        this.eliminado = false;
    }

    // Constructor 2: Para reconstruir el objeto que viene desde la base de datos
    public DepartamentoAcademico(Long id, Long facultadId, String abreviatura, String denominacion, Integer estado, Boolean eliminado) {
        this.id = id;
        setFacultadId(facultadId);
        setAbreviatura(abreviatura);
        setDenominacion(denominacion);
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

    public void actualizarDatos(String nuevaAbreviatura, String nuevaDenominacion) {
        setAbreviatura(nuevaAbreviatura);
        setDenominacion(nuevaDenominacion);
    }

    private void setFacultadId(Long facultadId) {
        if (facultadId == null || facultadId <= 0) {
            throw new IllegalArgumentException("El departamento debe estar asociado a una facultad válida.");
        }
        this.facultadId = facultadId;
    }

    private void setAbreviatura(String abreviatura) {
        if (abreviatura == null || abreviatura.trim().isEmpty()) {
            throw new IllegalArgumentException("La abreviatura del departamento no puede estar vacía.");
        }
        if (abreviatura.trim().length() > 10) {
            throw new IllegalArgumentException("La abreviatura no puede superar los 10 caracteres.");
        }
        this.abreviatura = abreviatura.trim().toUpperCase();
    }

    private void setDenominacion(String denominacion) {
        if (denominacion == null || denominacion.trim().isEmpty()) {
            throw new IllegalArgumentException("La denominación del departamento no puede estar vacía.");
        }
        this.denominacion = denominacion.trim();
    }

    // Getters
    public Long getId() { return id; }
    public Long getFacultadId() { return facultadId; }
    public String getAbreviatura() { return abreviatura; }
    public String getDenominacion() { return denominacion; }
    public Integer getEstado() { return estado; }
    public Boolean isEliminado() { return eliminado; }
}
