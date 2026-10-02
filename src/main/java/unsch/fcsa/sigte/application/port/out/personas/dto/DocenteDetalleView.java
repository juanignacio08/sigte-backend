package unsch.fcsa.sigte.application.port.out.personas.dto;

public record DocenteDetalleView(
        Long id,
        String codigo,
        Long escuelaId,
        String escuelaDenominacion,
        Long departamentoAcademicoId,
        String departamentoDenominacion,
        String tipoDocumentoCodigo,
        String tipoDocumentoDenominacion,
        String numeroDocumento,
        String nombres,
        String primerApellido,
        String segundoApellido,
        String correoInstitucional,
        String generoCodigo,
        String generoDenominacion,
        String telefono,
        Integer estado
) {
}
