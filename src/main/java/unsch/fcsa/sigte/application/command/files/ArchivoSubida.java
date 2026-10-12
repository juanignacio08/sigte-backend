package unsch.fcsa.sigte.application.command.files;

import java.util.Arrays;

public record ArchivoSubida(
        String nombreOriginal,
        String contentType,
        long tamanioBytes,
        byte[] contenido
) {
    public ArchivoSubida {
        if (nombreOriginal == null || nombreOriginal.isBlank()) {
            throw new IllegalArgumentException("El nombre del archivo es obligatorio.");
        }
        if (contenido == null || contenido.length == 0) {
            throw new IllegalArgumentException("El contenido del archivo no puede estar vacío.");
        }
    }

    public boolean esPdf() {
        return nombreOriginal.toLowerCase().endsWith(".pdf");
    }

    public boolean esWord() {
        return nombreOriginal.toLowerCase().endsWith(".doc") || nombreOriginal.toLowerCase().endsWith(".docx");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArchivoSubida other)) return false;
        return tamanioBytes == other.tamanioBytes
                && nombreOriginal.equals(other.nombreOriginal)
                && Arrays.equals(contenido, other.contenido);
    }

    @Override
    public int hashCode() {
        int result = nombreOriginal.hashCode();
        result = 31 * result + Long.hashCode(tamanioBytes);
        result = 31 * result + Arrays.hashCode(contenido);
        return result;
    }
}
