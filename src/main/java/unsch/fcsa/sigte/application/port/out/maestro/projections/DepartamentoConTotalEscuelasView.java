package unsch.fcsa.sigte.application.port.out.maestro.projections;

public interface DepartamentoConTotalEscuelasView {
    Long getId();
    Long getFacultadId();
    String getAbreviatura();
    String getDenominacion();
    Integer getEstado();
    Integer getTotalEscuelas();
}
