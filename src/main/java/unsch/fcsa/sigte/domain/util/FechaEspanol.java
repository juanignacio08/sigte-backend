package unsch.fcsa.sigte.domain.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FechaEspanol {
    private FechaEspanol() {}

    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("d 'de' MMMM 'del' yyyy", new Locale("es", "ES"));

    public static String formatear(LocalDate fecha) {
        if (fecha == null) return "";
        return fecha.format(FORMATO);
    }
}
