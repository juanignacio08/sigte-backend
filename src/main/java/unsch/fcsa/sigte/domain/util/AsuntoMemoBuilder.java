package unsch.fcsa.sigte.domain.util;

public class AsuntoMemoBuilder {
    private AsuntoMemoBuilder() {}

    public static String construir(String etapaCodigo, Integer numeroRevision) {
        String tipoDocumento = switch (etapaCodigo) {
            case "004001" -> "Proyecto";
            case "004002" -> "Borrador";
            case "004003" -> "Modificación de Proyecto";
            default -> "Documento";
        };

        int revision = numeroRevision != null ? numeroRevision : 1;

        if (revision <= 1) {
            return "Revisión y aprobación de " + tipoDocumento + " de tesis";
        }

        return OrdinalesEspanol.de(revision)
                + " revisión y aprobación de "
                + tipoDocumento
                + " de tesis";
    }
}
