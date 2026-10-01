package unsch.fcsa.sigte.domain.model.personas;

import unsch.fcsa.sigte.domain.exception.BusinessRuleException;

public class Docente {
    private Long id;
    private String codigo;
    private Long escuelaId;
    private Long departamentoAcademicoId;
    private String tipoDocumentoCodigo;
    private String numeroDocumento;
    private String nombres;
    private String primerApellido;
    private String segundoApellido;
    private String correoInstitucional;
    private String generoCodigo;
    private String telefono;
    private Integer estado;
    private Boolean eliminado;

    public Docente(
            String codigo,
            Long escuelaId,
            Long departamentoAcademicoId,
            String tipoDocumentoCodigo,
            String numeroDocumento,
            String nombres,
            String primerApellido,
            String segundoApellido,
            String correoInstitucional,
            String generoCodigo,
            String telefono
    ) {
        setCodigo(codigo);
        setEscuelaId(escuelaId);
        setDepartamentoAcademicoId(departamentoAcademicoId);
        setTipoDocumentoCodigo(tipoDocumentoCodigo);
        setNumeroDocumento(numeroDocumento);
        setNombres(nombres);
        setPrimerApellido(primerApellido);
        setSegundoApellido(segundoApellido);
        setCorreoInstitucional(correoInstitucional);
        setGeneroCodigo(generoCodigo);
        setTelefono(telefono);
        this.estado = 1;
        this.eliminado = false;
    }

    public Docente(
            Long id,
            String codigo,
            Long escuelaId,
            Long departamentoAcademicoId,
            String tipoDocumentoCodigo,
            String numeroDocumento,
            String nombres,
            String primerApellido,
            String segundoApellido,
            String correoInstitucional,
            String generoCodigo,
            String telefono,
            Integer estado,
            Boolean eliminado
    ) {
        this.id = id;
        setCodigo(codigo);
        setEscuelaId(escuelaId);
        setDepartamentoAcademicoId(departamentoAcademicoId);
        setTipoDocumentoCodigo(tipoDocumentoCodigo);
        setNumeroDocumento(numeroDocumento);
        setNombres(nombres);
        setPrimerApellido(primerApellido);
        setSegundoApellido(segundoApellido);
        setCorreoInstitucional(correoInstitucional);
        setGeneroCodigo(generoCodigo);
        setTelefono(telefono);
        this.estado = estado != null ? estado : 1;
        this.eliminado = eliminado != null && eliminado;
    }

    public void actualizarDatos(
            String codigo,
            Long escuelaId,
            Long departamentoAcademicoId,
            String tipoDocumentoCodigo,
            String numeroDocumento,
            String nombres,
            String primerApellido,
            String segundoApellido,
            String correoInstitucional,
            String generoCodigo,
            String telefono
    ) {
        setCodigo(codigo);
        setEscuelaId(escuelaId);
        setDepartamentoAcademicoId(departamentoAcademicoId);
        setTipoDocumentoCodigo(tipoDocumentoCodigo);
        setNumeroDocumento(numeroDocumento);
        setNombres(nombres);
        setPrimerApellido(primerApellido);
        setSegundoApellido(segundoApellido);
        setCorreoInstitucional(correoInstitucional);
        setGeneroCodigo(generoCodigo);
        setTelefono(telefono);
    }

    public void activar() {
        this.estado = 1;
    }

    public void desactivar() {
        this.estado = 0;
    }

    public void marcarComoEliminado() {
        this.eliminado = true;
        this.estado = 0;
    }

    private void setCodigo(String codigo) {
        if (codigo != null && codigo.trim().length() > 10) {
            throw new BusinessRuleException("El código del docente no puede superar los 10 caracteres.");
        }
        this.codigo = codigo != null ? codigo.trim() : null;
    }

    private void setEscuelaId(Long escuelaId) {
        if (escuelaId == null || escuelaId <= 0) {
            throw new BusinessRuleException("La escuela profesional es obligatoria.");
        }
        this.escuelaId = escuelaId;
    }

    private void setDepartamentoAcademicoId(Long departamentoAcademicoId) {
        if (departamentoAcademicoId == null || departamentoAcademicoId <= 0) {
            throw new BusinessRuleException("El departamento académico es obligatorio.");
        }
        this.departamentoAcademicoId = departamentoAcademicoId;
    }

    private void setTipoDocumentoCodigo(String tipoDocumentoCodigo) {
        if (tipoDocumentoCodigo != null && !tipoDocumentoCodigo.trim().isEmpty()) {
            String sanitized = tipoDocumentoCodigo.trim();
            if (sanitized.length() != 6) {
                throw new BusinessRuleException("El código de tipo de documento debe tener exactamente 6 dígitos.");
            }
            this.tipoDocumentoCodigo = sanitized;
        } else {
            this.tipoDocumentoCodigo = null;
        }
    }

    private void setNumeroDocumento(String numeroDocumento) {
        if (numeroDocumento != null && numeroDocumento.trim().length() > 15) {
            throw new BusinessRuleException("El número de documento no puede superar los 15 caracteres.");
        }
        this.numeroDocumento = numeroDocumento != null ? numeroDocumento.trim() : null;
    }

    private void setNombres(String nombres) {
        if (nombres == null || nombres.trim().isEmpty()) {
            throw new BusinessRuleException("El nombre del docente es obligatorio.");
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
        if (generoCodigo == null || generoCodigo.trim().isEmpty()) {
            throw new BusinessRuleException("El código de género es obligatorio.");
        }
        String sanitized = generoCodigo.trim();
        if (sanitized.length() != 6) {
            throw new BusinessRuleException("El código de género debe tener exactamente 6 dígitos.");
        }
        this.generoCodigo = sanitized;
    }

    private void setTelefono(String telefono) {
        if (telefono != null && telefono.trim().length() > 15) {
            throw new BusinessRuleException("El teléfono no puede superar los 15 caracteres.");
        }
        this.telefono = telefono != null ? telefono.trim() : null;
    }

    // Getters
    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public Long getEscuelaId() { return escuelaId; }
    public Long getDepartamentoAcademicoId() { return departamentoAcademicoId; }
    public String getTipoDocumentoCodigo() { return tipoDocumentoCodigo; }
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
