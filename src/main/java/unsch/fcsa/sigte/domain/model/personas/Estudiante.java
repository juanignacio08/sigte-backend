package unsch.fcsa.sigte.domain.model.personas;

import unsch.fcsa.sigte.domain.exception.BusinessRuleException;

public class Estudiante {

    private Long id;
    private Long escuelaId;
    private String codigo;
    private String numeroDocumento;
    private String nombres;
    private String primerApellido;
    private String segundoApellido;
    private String correoInstitucional;
    private String generoCodigo;
    private String telefono;
    private Integer estado;
    private Boolean eliminado;

    public Estudiante(
            Long escuelaId,
            String codigo,
            String numeroDocumento,
            String nombres,
            String primerApellido,
            String segundoApellido,
            String correoInstitucional,
            String generoCodigo,
            String telefono
    ) {
        setEscuelaId(escuelaId);
        setCodigo(codigo);
        setNumeroDocumento(numeroDocumento);
        setNombres(nombres);
        setPrimerApellido(primerApellido);
        setSegundoApellido(segundoApellido);
        setCorreoInstitucional(correoInstitucional);
        setGeneroCodigo(generoCodigo);
        this.estado = 1;
        this.eliminado = false;
    }

    public Estudiante(Long id, Long escuelaId, String codigo, String numeroDocumento,
                      String nombres, String primerApellido, String segundoApellido,
                      String correoInstitucional, String generoCodigo, String telefono,
                      Integer estado, Boolean eliminado) {
        this.id = id;
        setEscuelaId(escuelaId);
        setCodigo(codigo);
        setNumeroDocumento(numeroDocumento);
        setNombres(nombres);
        setPrimerApellido(primerApellido);
        setSegundoApellido(segundoApellido);
        setCorreoInstitucional(correoInstitucional);
        setGeneroCodigo(generoCodigo);
        setTelefono(telefono);
        this.estado = estado != null ? estado : 1;
        this.eliminado = eliminado != null ? eliminado : false;
    }

    public void actualizarDatos(
            Long escuelaId,
            String codigo,
            String numeroDocumento,
            String nombres,
            String primerApellido,
            String segundoApellido,
            String correoInstitucional,
            String generoCodigo,
            String telefono
    ) {
        setEscuelaId(escuelaId);
        setCodigo(codigo);
        setNumeroDocumento(numeroDocumento);
        setNombres(nombres);
        setPrimerApellido(primerApellido);
        setSegundoApellido(segundoApellido);
        setCorreoInstitucional(correoInstitucional);
        setGeneroCodigo(generoCodigo);
        setTelefono(telefono);
    }

    // Métodos de comportamiento
    public void activar() {
        this.estado = 1;
    }

    public void desactivar() {
        this.estado = 0;
    }

    public void eliminar() {
        this.eliminado = true;
        this.estado = 0;
    }

    // Setters con validaciones invariantes de dominio
    private void setEscuelaId(Long escuelaId) {
        if (escuelaId == null) {
            throw new BusinessRuleException("El ID de la escuela profesional es obligatorio.");
        }
        this.escuelaId = escuelaId;
    }

    private void setCodigo(String codigo) {
        this.codigo = (codigo != null && !codigo.isBlank()) ? codigo.trim() : null;
    }

    private void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = (numeroDocumento != null && !numeroDocumento.isBlank()) ? numeroDocumento.trim() : null;
    }

    private void setNombres(String nombres) {
        if (nombres == null || nombres.isBlank()) {
            throw new BusinessRuleException("El nombre del estudiante es obligatorio.");
        }
        if (nombres.trim().length() > 100) {
            throw new BusinessRuleException("El nombre del docente no puede superar los 100 caracteres.");
        }
        this.nombres = nombres.trim();
    }

    private void setPrimerApellido(String primerApellido) {
        if (primerApellido == null || primerApellido.trim().isEmpty()) {
            throw new BusinessRuleException("El primer apellido del docente es obligatorio.");
        }
        if (primerApellido.trim().length() > 100) {
            throw new BusinessRuleException("El primer apellido no puede superar los 100 caracteres.");
        }
        this.primerApellido = primerApellido.trim();
    }

    private void setSegundoApellido(String segundoApellido) {
        if (segundoApellido == null || segundoApellido.trim().isEmpty()) {
            throw new BusinessRuleException("El segundo apellido del docente es obligatorio.");
        }
        if (segundoApellido.trim().length() > 100) {
            throw new BusinessRuleException("El segundo apellido no puede superar los 100 caracteres.");
        }
        this.segundoApellido = segundoApellido.trim();
    }

    private void setCorreoInstitucional(String correoInstitucional) {
        if (correoInstitucional == null || correoInstitucional.trim().isEmpty()) {
            throw new BusinessRuleException("El correo institucional es obligatorio.");
        }
        if (correoInstitucional.trim().length() > 100) {
            throw new BusinessRuleException("El correo institucional no puede superar los 100 caracteres.");
        }
        this.correoInstitucional = correoInstitucional.trim().toLowerCase();
    }

    private void setGeneroCodigo(String generoCodigo) {
        if (generoCodigo == null || generoCodigo.isBlank()) {
            throw new BusinessRuleException("El código de género es obligatorio.");
        }
        if (generoCodigo.trim().length() != 6) {
            throw new BusinessRuleException("El código de género debe tener exactamente 6 caracteres.");
        }
        this.generoCodigo = generoCodigo.trim();
    }

    private void setTelefono(String telefono) {
        if (telefono != null && telefono.trim().length() > 15) {
            throw new BusinessRuleException("El teléfono no puede superar los 15 caracteres.");
        }
        this.telefono = telefono != null ? telefono.trim() : null;
    }

    // Getters
    public Long getId() { return id; }
    public Long getEscuelaId() { return escuelaId; }
    public String getCodigo() { return codigo; }
    public String getNumeroDocumento() { return numeroDocumento; }
    public String getNombres() { return nombres; }
    public String getPrimerApellido() { return primerApellido; }
    public String getSegundoApellido() { return segundoApellido; }
    public String getCorreoInstitucional() { return correoInstitucional; }
    public String getGeneroCodigo() { return generoCodigo; }
    public String getTelefono() { return telefono; }
    public Integer getEstado() { return estado; }
    public Boolean isEliminado() { return eliminado; }
}