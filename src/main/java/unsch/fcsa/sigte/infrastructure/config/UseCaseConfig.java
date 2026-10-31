package unsch.fcsa.sigte.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import unsch.fcsa.sigte.application.port.in.maestro.GestionarDepartamentoUseCase;
import unsch.fcsa.sigte.application.port.in.maestro.GestionarEscuelaUseCase;
import unsch.fcsa.sigte.application.port.in.maestro.GestionarFacultadUseCase;
import unsch.fcsa.sigte.application.port.in.maestro.GestionarMaestrosUseCase;
import unsch.fcsa.sigte.application.port.in.personas.GestionarDocenteUseCase;
import unsch.fcsa.sigte.application.port.in.personas.GestionarEstudianteUseCase;
import unsch.fcsa.sigte.application.port.in.tramites.GestionarComisionUseCase;
import unsch.fcsa.sigte.application.port.in.tramites.GestionarDocumentosTesisUseCase;
import unsch.fcsa.sigte.application.port.in.tramites.GestionarExpedienteUseCase;
import unsch.fcsa.sigte.application.port.in.tramites.GestionarTesisUseCase;
import unsch.fcsa.sigte.application.port.out.maestro.FacultadRepositoryPort;
import unsch.fcsa.sigte.application.port.out.maestro.MaestroRepositoryPort;
import unsch.fcsa.sigte.application.port.out.personas.DocenteRepositoryPort;
import unsch.fcsa.sigte.application.port.out.personas.EstudianteRepositoryPort;
import unsch.fcsa.sigte.application.port.out.storage.StoragePort;
import unsch.fcsa.sigte.application.port.out.tramites.*;
import unsch.fcsa.sigte.application.usecase.maestro.GestionarDepartamentoUseCaseImpl;
import unsch.fcsa.sigte.application.usecase.maestro.GestionarEscuelaUseCaseImpl;
import unsch.fcsa.sigte.application.port.out.maestro.DepartamentoRepositoryPort;
import unsch.fcsa.sigte.application.port.out.maestro.EscuelaProfesionalRepositoryPort;
import unsch.fcsa.sigte.application.usecase.maestro.GestionarFacultadUseCaseImpl;
import unsch.fcsa.sigte.application.usecase.maestro.GestionarMaestroUseCaseImpl;
import unsch.fcsa.sigte.application.usecase.personas.GestionarDocenteUseCaseImpl;
import unsch.fcsa.sigte.application.usecase.personas.GestionarEstudianteUseCaseImpl;
import unsch.fcsa.sigte.application.usecase.tramites.GestionarComisionUseCaseImpl;
import unsch.fcsa.sigte.application.usecase.tramites.GestionarDocumentosTesisUseCaseImpl;
import unsch.fcsa.sigte.application.usecase.tramites.GestionarExpedienteUseCaseImpl;
import unsch.fcsa.sigte.application.usecase.tramites.GestionarTesisUseCaseImpl;

@Configuration
public class UseCaseConfig {

    @Bean
    public GestionarMaestrosUseCase gestionarMaestrosUseCase(MaestroRepositoryPort maestroRepositoryPort) {
        return new GestionarMaestroUseCaseImpl(maestroRepositoryPort);
    }

    @Bean
    public GestionarFacultadUseCase gestionarFacultadUseCase(FacultadRepositoryPort facultadRepositoryPort) {
        return new GestionarFacultadUseCaseImpl(facultadRepositoryPort);
    }

    @Bean
    public GestionarDepartamentoUseCase gestionarDepartamentoUseCase(DepartamentoRepositoryPort departamentoRepositoryPort, FacultadRepositoryPort facultadRepositoryPort, EscuelaProfesionalRepositoryPort escuelaProfesionalRepositoryPort) {
        return new GestionarDepartamentoUseCaseImpl(departamentoRepositoryPort, facultadRepositoryPort, escuelaProfesionalRepositoryPort);
    }

    @Bean
    public GestionarEscuelaUseCase gestionarEscuelaUseCase(
            EscuelaProfesionalRepositoryPort escuelaProfesionalRepositoryPort,
            DepartamentoRepositoryPort departamentoRepositoryPort,
            StoragePort storagePort
    ) {
        return new GestionarEscuelaUseCaseImpl(
                escuelaProfesionalRepositoryPort,
                departamentoRepositoryPort,
                storagePort
        );
    }

    @Bean
    public GestionarDocenteUseCase gestionarDocenteUseCase(
            DocenteRepositoryPort docenteRepositoryPort,
            DepartamentoRepositoryPort departamentoRepositoryPort,
            EscuelaProfesionalRepositoryPort escuelaProfesionalRepositoryPort,
            MaestroRepositoryPort maestroRepositoryPort
    ) {
        return new GestionarDocenteUseCaseImpl(
                docenteRepositoryPort,
                departamentoRepositoryPort,
                escuelaProfesionalRepositoryPort,
                maestroRepositoryPort
        );
    }

    @Bean
    public GestionarEstudianteUseCase gestionarEstudianteUseCase(
            EstudianteRepositoryPort estudianteRepositoryPort,
            EscuelaProfesionalRepositoryPort escuelaProfesionalRepositoryPort) {
        return new GestionarEstudianteUseCaseImpl(estudianteRepositoryPort, escuelaProfesionalRepositoryPort);
    }

    @Bean
    public GestionarTesisUseCase gestionarTesisUseCase(
            TesisRepositoryPort tesisRepositoryPort,
            EscuelaProfesionalRepositoryPort escuelaProfesionalRepositoryPort,
            EstudianteRepositoryPort estudianteRepositoryPort,
            GestionarExpedienteUseCase gestionarExpedienteUseCase) {
        return new GestionarTesisUseCaseImpl(
                tesisRepositoryPort,
                escuelaProfesionalRepositoryPort,
                estudianteRepositoryPort,
                gestionarExpedienteUseCase
        );
    }

    @Bean
    public GestionarExpedienteUseCase gestionarExpedienteUseCase(
            ExpedienteRepositoryPort expedienteRepositoryPort,
            ExpedienteArchivoRepositoryPort expedienteArchivoRepositoryPort,
            TesisRepositoryPort tesisRepositoryPort,
            StoragePort storagePort) {
        return new GestionarExpedienteUseCaseImpl(
                expedienteRepositoryPort, expedienteArchivoRepositoryPort,
                tesisRepositoryPort, storagePort
        );
    }

    @Bean
    public GestionarComisionUseCase gestionarComisionUseCase(
            ComisionRepositoryPort comisionRepositoryPort,
            ComisionMiembroRepositoryPort comisionMiembroRepositoryPort,
            TesisRepositoryPort tesisRepositoryPort,
            DocenteRepositoryPort docenteRepositoryPort
    ) {
        return new GestionarComisionUseCaseImpl(
                comisionRepositoryPort, comisionMiembroRepositoryPort,
                tesisRepositoryPort, docenteRepositoryPort
        );
    }

    @Bean
    public GestionarDocumentosTesisUseCase gestionarDocumentosTesisUseCase(
            DocumentoTesisRepositoryPort documentoTesisRepositoryPort) {
        return new GestionarDocumentosTesisUseCaseImpl(documentoTesisRepositoryPort);
    }

}
