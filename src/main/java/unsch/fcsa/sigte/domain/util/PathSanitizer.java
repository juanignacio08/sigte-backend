package unsch.fcsa.sigte.domain.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class PathSanitizer {

    private PathSanitizer() {}

    public static String sanitizeRelativePath(String relativePath) {
        if (relativePath == null || relativePath.isBlank()) {
            throw new IllegalArgumentException("La ruta relativa no puede estar vacía.");
        }

        String clean = relativePath.replaceAll("[\\r\\n\\t\\x00]", "").trim();
        clean = Paths.get(clean).normalize().toString().replace('\\', '/');

        while (clean.startsWith("/")) {
            clean = clean.substring(1);
        }

        Path baseDummy = Paths.get("/app/storage").toAbsolutePath().normalize();
        Path resolved = baseDummy.resolve(clean).normalize();

        if (!resolved.startsWith(baseDummy)) {
            throw new SecurityException("Intento de Path Traversal detectado en la ruta: " + relativePath);
        }

        return clean;
    }
}
