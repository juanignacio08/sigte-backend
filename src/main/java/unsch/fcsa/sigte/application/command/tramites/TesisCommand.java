package unsch.fcsa.sigte.application.command.tramites;

import java.util.List;
import java.util.UUID;

public record TesisCommand(
        UUID id,
        Long escuelaId,
        String titulo,
        List<Long> estudiantesIds,
        String numeroExpediente,
        String etapaTesis
) {
    public static TesisCommand create(
            Long escuelaId,
            String titulo,
            List<Long> estudiantesIds,
            String numeroExpediente,
            String etapaTesis
    ) {
        return new TesisCommand(
                null,
                escuelaId,
                titulo,
                estudiantesIds,
                numeroExpediente,
                etapaTesis
        );
    }

    public static TesisCommand update(
            UUID id,
            Long escuelaId,
            String titulo,
            List<Long> estudiantesIds,
            String etapaTesis
    ) {
        return new TesisCommand(
                id,
                escuelaId,
                titulo,
                estudiantesIds,
                null,
                etapaTesis
        );
    }
}
