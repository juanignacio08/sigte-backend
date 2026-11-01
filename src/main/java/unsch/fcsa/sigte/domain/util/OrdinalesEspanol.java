package unsch.fcsa.sigte.domain.util;

public class OrdinalesEspanol {

    private OrdinalesEspanol() {}

    private static final String[] ORDINALES = {
            "",           // 0
            "Primera",    // 1
            "Segunda",    // 2
            "Tercera",    // 3
            "Cuarta",     // 4
            "Quinta",     // 5
            "Sexta",      // 6
            "Séptima",    // 7
            "Octava",     // 8
            "Novena",     // 9
            "Décima",     // 10
    };

    public static String de(int numero) {
        if (numero >= 1 && numero < ORDINALES.length) {
            return ORDINALES[numero];
        }
        return "N° " + numero;
    }

}
