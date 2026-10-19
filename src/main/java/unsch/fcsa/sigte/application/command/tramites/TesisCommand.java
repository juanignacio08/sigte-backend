package unsch.fcsa.sigte.application.command.tramites;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record TesisCommand(
        UUID id,
        Long escuelaId,
        String titulo,
        List<Long> estudiantesIds,
        String numeroExpediente,
        String etapaTesis,
        OffsetDateTime fechaIngreso,
        Integer numeroRevision
) {
    public static TesisCommand create(
            Long escuelaId,
            String titulo,
            List<Long> estudiantesIds,
            String numeroExpediente,
            String etapaTesis,
            OffsetDateTime fechaIngreso,
            Integer numeroRevision
    ) {
        return new TesisCommand(
                null,
                escuelaId,
                titulo,
                estudiantesIds,
                numeroExpediente,
                etapaTesis,
                fechaIngreso,
                numeroRevision
        );
    }

    public static TesisCommand update(
            UUID id,
            Long escuelaId,
            String titulo,
            List<Long> estudiantesIds,
            String etapaTesis,
            OffsetDateTime fechaIngreso,
            Integer numeroRevision
    ) {
        return new TesisCommand(
                id,
                escuelaId,
                titulo,
                estudiantesIds,
                null,
                etapaTesis,
                fechaIngreso,
                numeroRevision
        );
    }
}
