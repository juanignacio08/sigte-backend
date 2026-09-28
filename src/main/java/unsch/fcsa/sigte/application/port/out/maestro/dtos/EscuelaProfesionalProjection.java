package unsch.fcsa.sigte.application.port.out.maestro.dtos;

public interface EscuelaProfesionalProjection {
    Long getId();
    Integer getCodigo();
    String getAbreviatura();
    String getDenominacion();
    String getPlantillaMemorando();
    String getPlantillaResolucion();
    Integer getEstado();

    Long getDepartamentoId();
    String getDepartamentoDenominacion();

}
