package unsch.fcsa.sigte.application.port.out.tramites.dto;

public record DocumentoGenerado(
        String rutaWord,
        String rutaPdf,
        Long tamanioWordBytes,
        Long tamanioPdfBytes
) {}
