package unsch.fcsa.sigte.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;
import unsch.fcsa.sigte.application.port.in.maestro.*;
import unsch.fcsa.sigte.application.port.in.personas.GestionarDocenteUseCase;
import unsch.fcsa.sigte.application.port.in.personas.GestionarEstudianteUseCase;
import unsch.fcsa.sigte.application.port.in.tramites.*;
import unsch.fcsa.sigte.application.port.out.maestro.*;
import unsch.fcsa.sigte.application.port.out.notificaciones.EmailPort;
import unsch.fcsa.sigte.application.port.out.personas.DocenteRepositoryPort;
import unsch.fcsa.sigte.application.port.out.personas.EstudianteRepositoryPort;
import unsch.fcsa.sigte.application.port.out.storage.DocumentoGeneradorPort;
import unsch.fcsa.sigte.application.port.out.storage.StoragePort;
import unsch.fcsa.sigte.application.port.out.tramites.*;
import unsch.fcsa.sigte.application.usecase.maestro.*;
import unsch.fcsa.sigte.application.usecase.personas.GestionarDocenteUseCaseImpl;
import unsch.fcsa.sigte.application.usecase.personas.GestionarEstudianteUseCaseImpl;
import unsch.fcsa.sigte.application.usecase.tramites.*;

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
            DepartamentoRepositoryPort departamentoRepositoryPort
    ) {
        return new GestionarEscuelaUseCaseImpl(
                escuelaProfesionalRepositoryPort,
                departamentoRepositoryPort
        );
    }

    @Bean
    public GestionarPlantillaDocumentoUseCase gestionarPlantillaDocumentoUseCase(
            PlantillaDocumentoRepositoryPort plantillaDocumentoRepositoryPort,
            StoragePort storagePort
    ) {
        return new GestionarPlantillaDocumentoUseCaseImpl(plantillaDocumentoRepositoryPort, storagePort);
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
            ComisionRepositoryPort comisionRepositoryPort,
            GestionarExpedienteUseCase gestionarExpedienteUseCase) {
        return new GestionarTesisUseCaseImpl(
                tesisRepositoryPort,
                escuelaProfesionalRepositoryPort,
                estudianteRepositoryPort,
                comisionRepositoryPort,
                gestionarExpedienteUseCase
        );
    }

    @Bean
    public GestionarExpedienteUseCase gestionarExpedienteUseCase(
            ExpedienteRepositoryPort expedienteRepositoryPort,
            ExpedienteArchivoRepositoryPort expedienteArchivoRepositoryPort,
            TesisRepositoryPort tesisRepositoryPort,
            DocumentoTesisRepositoryPort documentoTesisRepositoryPort,
            ComisionRepositoryPort comisionRepositoryPort,
            ComisionMiembroRepositoryPort comisionMiembroRepositoryPort,
            StoragePort storagePort) {
        return new GestionarExpedienteUseCaseImpl(
                expedienteRepositoryPort, expedienteArchivoRepositoryPort,
                tesisRepositoryPort, documentoTesisRepositoryPort,
                comisionRepositoryPort, comisionMiembroRepositoryPort, storagePort
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
            DocumentoTesisRepositoryPort documentoTesisRepositoryPort,
            DocumentoGeneradorPort documentoGeneradorPort,
            TesisRepositoryPort tesisRepositoryPort,
            ExpedienteRepositoryPort expedienteRepositoryPort,
            ExpedienteArchivoRepositoryPort expedienteArchivoRepositoryPort,
            EmailPort emailPort,
            EnvioCorreoRepositoryPort envioCorreoRepositoryPort,
            GestionarComisionUseCase gestionarComisionUseCase,
            StoragePort storagePort,
            PlantillaDocumentoRepositoryPort plantillaDocumentoRepositoryPort,
            ObjectMapper objectMapper
    ) {
        return new GestionarDocumentosTesisUseCaseImpl(
                documentoTesisRepositoryPort,
                documentoGeneradorPort,
                tesisRepositoryPort,
                expedienteRepositoryPort,
                expedienteArchivoRepositoryPort,
                emailPort,
                envioCorreoRepositoryPort,
                gestionarComisionUseCase,
                storagePort,
                plantillaDocumentoRepositoryPort,
                objectMapper
        );
    }

    @Bean
    public GestionarEnvioCorreoUseCase gestionarEnvioCorreoUseCase(
            EnvioCorreoRepositoryPort envioCorreoRepositoryPort
    ) {
        return new GestionarEnvioCorreoUseCaseImpl(
                envioCorreoRepositoryPort
        );
    }

}
