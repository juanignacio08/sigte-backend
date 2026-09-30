package unsch.fcsa.sigte.application.port.out.maestro.dtos;

public interface DepartamentoConTotalEscuelasView {
    Long getId();
    Long getFacultadId();
    String getAbreviatura();
    String getDenominacion();
    Integer getEstado();
    Integer getTotalEscuelas();
}
