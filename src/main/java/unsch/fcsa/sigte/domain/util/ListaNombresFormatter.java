package unsch.fcsa.sigte.domain.util;

import java.util.List;

/**
 * Une nombres con comas y "y" antes del último.
 * Ej: [A] → "A" · [A, B] → "A y B" · [A, B, C] → "A, B y C"
 */
public final class ListaNombresFormatter {

    private ListaNombresFormatter() {}

    public static String unir(List<String> nombres) {
        if (nombres == null || nombres.isEmpty()) return "";
        if (nombres.size() == 1) return nombres.get(0);
        if (nombres.size() == 2) return nombres.get(0) + " y " + nombres.get(1);

        String todosMenosElUltimo = String.join(", ",
                nombres.subList(0, nombres.size() - 1));
        return todosMenosElUltimo + " y " + nombres.getLast();
    }
}