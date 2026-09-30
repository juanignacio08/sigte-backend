package unsch.fcsa.sigte.domain.model.maestro;

public class Maestro {
    private Long id;
    private String codigoTabla;
    private String codigoItem;
    private String codigo;
    private String denominacion;
    private Boolean esSistema;
    private Integer estado;
    private Boolean eliminado;

    public Maestro(String codigoTabla, String codigoItem, String denominacion, Boolean esSistema) {
        setCodigoTabla(codigoTabla);
        setCodigoItem(codigoItem);
        this.codigo = this.codigoTabla + this.codigoItem;
        setDenominacion(denominacion);
        this.esSistema = esSistema == null || esSistema;
        this.estado = 1;
        this.eliminado = false;
    }

    public Maestro(Long id, String codigoTabla, String codigoItem, String codigo, String denominacion, Boolean esSistema, Integer estado, Boolean eliminado) {
        this.id = id;
        setCodigoTabla(codigoTabla);
        setCodigoItem(codigoItem);
        this.codigo = codigo != null ? codigo : (this.codigoTabla + this.codigoItem);
        setDenominacion(denominacion);
        this.esSistema = esSistema == null || esSistema;
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

    public void actualizarDatos(String denominacion, Boolean esSistema) {
        setDenominacion(denominacion);
        esSistema = esSistema == null || esSistema;
    }

    private void setCodigoTabla(String codigoTabla) {
        if (codigoTabla == null || codigoTabla.trim().length() != 3) {
            throw new IllegalArgumentException("El código de tabla debe tener exactamente 3 caracteres.");
        }
        this.codigoTabla = codigoTabla.trim();
    }

    private void setCodigoItem(String codigoItem) {
        if (codigoItem == null || codigoItem.trim().length() != 3) {
            throw new IllegalArgumentException("El código de ítem debe tener exactamente 3 caracteres.");
        }
        this.codigoItem = codigoItem.trim();
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

    // Getters
    public Long getId() { return id; }
    public String getCodigoTabla() { return codigoTabla; }
    public String getCodigoItem() { return codigoItem; }
    public String getCodigo() { return codigo; }
    public String getDenominacion() { return denominacion; }
    public Boolean getEsSistema() { return esSistema; }
    public Integer getEstado() { return estado; }
    public Boolean isEliminado() { return eliminado; }
}