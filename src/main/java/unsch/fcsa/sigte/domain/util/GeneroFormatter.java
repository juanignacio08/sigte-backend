package unsch.fcsa.sigte.domain.util;

import java.util.List;

public final class GeneroFormatter {

    private GeneroFormatter() {}

    private static boolean esFemenino(String generoCodigo) {
        return TablaMaestraCodigos.GENERO_FEMENINO.equals(generoCodigo);
    }

    private static boolean todosFemeninos(List<String> generos) {
        return generos != null && !generos.isEmpty()
                && generos.stream().allMatch(GeneroFormatter::esFemenino);
    }

    // ==================================================================
    // AUTORES / ESTUDIANTES
    // ==================================================================

    /** "alumna" | "alumno" | "alumnas" | "alumnos" */
    public static String graduado(List<String> generos) {
        if (generos == null || generos.isEmpty()) { return ""; }
        if (generos.size() == 1) {
            return esFemenino(generos.getFirst()) ? "alumna" : "alumno";
        }
        return todosFemeninos(generos) ? "alumnas" : "alumnos";
    }

    /** "la alumna" | "el alumno" | "las alumnas" | "los alumnos" */
    public static String losAutores(List<String> generos) {
        if (generos == null || generos.isEmpty()) return "";

        if (generos.size() == 1) {
            return esFemenino(generos.getFirst()) ? "la alumna cumple" : "el alumno cumple";
        }

        return todosFemeninos(generos) ? "las alumnas cumplen" : "los alumnos cumplen";
    }

    // ==================================================================
    // PRESIDENTE / ASESOR (individual, pero admite plural)
    // ==================================================================

    /** "de la profesora" | "del profesor" | "de las profesoras" | "de los profesores" */
    public static String deProfesor(List<String> generos) {
        if (generos == null || generos.isEmpty()) return "";

        if (generos.size() == 1) {
            return esFemenino(generos.getFirst()) ? "de la profesora" : "del profesor";
        }

        return todosFemeninos(generos) ? "de las profesoras" : "de los profesores";
    }

    /** "a la profesora" | "al profesor" | "a las profesoras" | "a los profesores" */
    public static String aProfesor(List<String> generos) {
        if (generos == null || generos.isEmpty()) return "";
        if (generos.size() == 1) {
            return esFemenino(generos.getFirst()) ? "a la profesora" : "al profesor";
        }
        return todosFemeninos(generos) ? "a las profesoras" : "a los profesores";
    }

    /** "la profesora" | "el profesor" | "las profesoras" | "los profesores" (para jurados) */
    public static String lasProfesoras(List<String> generos) {
        if (generos == null || generos.isEmpty()) return "";

        if (generos.size() == 1) {
            return esFemenino(generos.getFirst()) ? "la profesora" : "el profesor";
        }

        return todosFemeninos(generos) ? "las profesoras" : "los profesores";
    }

    /** "asesora" | "asesor" | "asesoras" | "asesores" */
    public static String asesorSustantivo(List<String> generos) {
        if (generos == null || generos.isEmpty()) return "";
        if (generos.size() == 1) {
            return esFemenino(generos.getFirst()) ? "asesora" : "asesor";
        }
        return todosFemeninos(generos) ? "asesoras" : "asesores";
    }
}