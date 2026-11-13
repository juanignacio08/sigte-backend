package unsch.fcsa.sigte.infrastructure.adapter.input.rest.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String codigo,
        String mensaje,
        int estadoStatus,
        LocalDateTime timestamp,
        Object detalles
) {
    public static ErrorResponse build(String codigo, String mensaje, int status, Object detalles) {
        return new ErrorResponse(codigo, mensaje, status, LocalDateTime.now(), detalles);
    }
}
