package unsch.fcsa.sigte.application.command.personas;

public record DocenteCommand(
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
        String telefono
) {
    public static DocenteCommand create(
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
        return new DocenteCommand(
                null,
                codigo,
                escuelaId,
                departamentoAcademicoId,
                tipoDocumentoCodigo,
                numeroDocumento,
                nombres,
                primerApellido,
                segundoApellido,
                correoInstitucional,
                generoCodigo,
                telefono
        );
    }

    public static DocenteCommand update(
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
            String telefono
    ) {
        return new DocenteCommand(
                id,
                codigo,
                escuelaId,
                departamentoAcademicoId,
                tipoDocumentoCodigo,
                numeroDocumento,
                nombres,
                primerApellido,
                segundoApellido,
                correoInstitucional,
                generoCodigo,
                telefono
        );
    }
}
