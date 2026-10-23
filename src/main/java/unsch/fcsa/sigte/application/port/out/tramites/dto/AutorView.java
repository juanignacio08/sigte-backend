package unsch.fcsa.sigte.application.port.out.tramites.dto;

import java.time.OffsetDateTime;

public record AutorView(Long id, Long estudianteId, String numeroDocumento, String nombres, String primerApellido,
                        String segundoApellido, String correoInstitucional, OffsetDateTime fechaAsociacion,
                        Integer estado) {
}