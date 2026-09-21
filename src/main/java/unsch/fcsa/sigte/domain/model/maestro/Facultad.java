package unsch.fcsa.sigte.domain.model.maestro;

public class Facultad {
    private Long id;
    private String abreviatura;
    private String denominacion;
    private Integer estado;
    private Boolean eliminado;

    public Facultad(String abreviatura, String denominacion) {
        setAbreviatura(abreviatura);
        setDenominacion(denominacion);
        estado = 1;
        eliminado = false;
    }

    public Facultad(Long id, String abreviatura, String denominacion, Integer estado, Boolean eliminado) {
        this.id = id;
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

    private void setAbreviatura(String abreviatura) {
        if (abreviatura == null || abreviatura.trim().isEmpty()) {
            throw new IllegalArgumentException("La abreviatura de la Facultad no puede estar vacía.");
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
        this.denominacion = denominacion.trim();
    }

    public Long getId() { return id; }
    public String getAbreviatura() { return abreviatura; }
    public String getDenominacion() { return denominacion; }
    public Integer getEstado() { return estado; }
    public Boolean isEliminado() { return eliminado; }

}
