package unsch.fcsa.sigte.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import unsch.fcsa.sigte.application.port.in.maestro.GestionarDepartamentoUseCase;
import unsch.fcsa.sigte.application.port.in.maestro.GestionarEscuelaUseCase;
import unsch.fcsa.sigte.application.port.in.maestro.GestionarFacultadUseCase;
import unsch.fcsa.sigte.application.port.out.maestro.FacultadRepositoryPort;
import unsch.fcsa.sigte.application.usecase.maestro.GestionarDepartamentoUseCaseImpl;
import unsch.fcsa.sigte.application.usecase.maestro.GestionarEscuelaUseCaseImpl;
import unsch.fcsa.sigte.application.port.out.maestro.DepartamentoRepositoryPort;
import unsch.fcsa.sigte.application.port.out.maestro.EscuelaProfesionalRepositoryPort;
import unsch.fcsa.sigte.application.usecase.maestro.GestionarFacultadUseCaseImpl;

@Configuration
public class UseCaseConfig {

    @Bean
    public GestionarFacultadUseCase gestionarFacultadUseCase(FacultadRepositoryPort facultadRepositoryPort) {
        return new GestionarFacultadUseCaseImpl(facultadRepositoryPort);
    }

    @Bean
    public GestionarDepartamentoUseCase gestionarDepartamentoUseCase(DepartamentoRepositoryPort departamentoRepositoryPort, FacultadRepositoryPort facultadRepositoryPort, EscuelaProfesionalRepositoryPort escuelaProfesionalRepositoryPort) {
        return new GestionarDepartamentoUseCaseImpl(departamentoRepositoryPort, facultadRepositoryPort, escuelaProfesionalRepositoryPort);
    }

    @Bean
    public GestionarEscuelaUseCase gestionarEscuelaUseCase(EscuelaProfesionalRepositoryPort escuelaProfesionalRepositoryPort, DepartamentoRepositoryPort departamentoRepositoryPort) {
        return new GestionarEscuelaUseCaseImpl(escuelaProfesionalRepositoryPort, departamentoRepositoryPort);
    }

}
