package unsch.fcsa.sigte.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import unsch.fcsa.sigte.domain.port.in.maestro.GestionarDepartamentoUseCase;
import unsch.fcsa.sigte.domain.port.in.maestro.GestionarEscuelaUseCase;
import unsch.fcsa.sigte.application.usecase.maestro.GestionarDepartamentoUseCaseImpl;
import unsch.fcsa.sigte.application.usecase.maestro.GestionarEscuelaUseCaseImpl;
import unsch.fcsa.sigte.domain.port.out.maestro.DepartamentoRepositoryPort;
import unsch.fcsa.sigte.domain.port.out.maestro.EscuelaProfesionalRepositoryPort;

@Configuration
public class UseCaseConfig {

    @Bean
    public GestionarDepartamentoUseCase gestionarDepartamentoUseCase(DepartamentoRepositoryPort departamentoRepositoryPort) {
        return new GestionarDepartamentoUseCaseImpl(departamentoRepositoryPort);
    }

    @Bean
    public GestionarEscuelaUseCase gestionarEscuelaUseCase(EscuelaProfesionalRepositoryPort escuelaProfesionalRepositoryPort, DepartamentoRepositoryPort departamentoRepositoryPort) {
        return new GestionarEscuelaUseCaseImpl(escuelaProfesionalRepositoryPort, departamentoRepositoryPort);
    }
}
