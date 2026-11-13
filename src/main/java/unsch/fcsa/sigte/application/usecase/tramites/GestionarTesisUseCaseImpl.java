package unsch.fcsa.sigte.application.usecase.tramites;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import unsch.fcsa.sigte.application.command.PageResult;
import unsch.fcsa.sigte.application.command.files.ArchivoSubida;
import unsch.fcsa.sigte.application.command.tramites.RegistrarExpedienteCommand;
import unsch.fcsa.sigte.application.command.tramites.TesisCommand;
import unsch.fcsa.sigte.application.command.tramites.TesisFiltro;
import unsch.fcsa.sigte.application.port.in.tramites.GestionarExpedienteUseCase;
import unsch.fcsa.sigte.application.port.in.tramites.GestionarTesisUseCase;
import unsch.fcsa.sigte.application.port.out.maestro.EscuelaProfesionalRepositoryPort;
import unsch.fcsa.sigte.application.port.out.personas.EstudianteRepositoryPort;
import unsch.fcsa.sigte.application.port.out.tramites.ComisionRepositoryPort;
import unsch.fcsa.sigte.application.port.out.tramites.TesisRepositoryPort;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ExpedienteDetalleView;
import unsch.fcsa.sigte.application.port.out.tramites.dto.TesisDetalleView;
import unsch.fcsa.sigte.domain.exception.BusinessRuleException;
import unsch.fcsa.sigte.domain.exception.ResourceNotFoundException;
import unsch.fcsa.sigte.domain.model.tramites.Comision;
import unsch.fcsa.sigte.domain.model.tramites.Tesis;
import unsch.fcsa.sigte.domain.model.tramites.TesisAutor;
import unsch.fcsa.sigte.domain.util.TablaMaestraCodigos;

import java.time.OffsetDateTime;
import java.util.*;

@Slf4j
public class GestionarTesisUseCaseImpl implements GestionarTesisUseCase {

    private final TesisRepositoryPort tesisRepositoryPort;
    private final EscuelaProfesionalRepositoryPort escuelaProfesionalRepositoryPort;
    private final EstudianteRepositoryPort estudianteRepositoryPort;
    private final ComisionRepositoryPort comisionRepositoryPort;
    private final GestionarExpedienteUseCase gestionarExpedienteUseCase;

    public GestionarTesisUseCaseImpl(
            TesisRepositoryPort tesisRepositoryPort,
            EscuelaProfesionalRepositoryPort escuelaProfesionalRepositoryPort,
            EstudianteRepositoryPort estudianteRepositoryPort,
            ComisionRepositoryPort comisionRepositoryPort,
            GestionarExpedienteUseCase gestionarExpedienteUseCase
    ){
        this.tesisRepositoryPort = tesisRepositoryPort;
        this.escuelaProfesionalRepositoryPort = escuelaProfesionalRepositoryPort;
        this.estudianteRepositoryPort = estudianteRepositoryPort;
        this.comisionRepositoryPort = comisionRepositoryPort;
        this.gestionarExpedienteUseCase = gestionarExpedienteUseCase;
    }

    @Override
    @Transactional
    public Tesis registrarProyecto(TesisCommand command, List<ArchivoSubida> files) {
        log.info("Registrando un proyecto de tesis. [ TESIS : {} ]", command.titulo());

        if (!escuelaProfesionalRepositoryPort.existePorId(command.escuelaId())) {
            throw new ResourceNotFoundException("Escuela", "id", command.escuelaId());
        }

        validarListaEstudiantes(command.estudiantesIds());

        for (Long estudianteId : command.estudiantesIds()) {
            if (tesisRepositoryPort.estudianteTieneTesisActiva(estudianteId)) {
                throw new BusinessRuleException("El estudiante con ID " + estudianteId + " ya cuenta con una tesis activa.");
            }
        }

        Tesis tesis = new Tesis(
                null,
                command.escuelaId(),
                command.titulo(),
                null,
                TablaMaestraCodigos.TESIS_ESTADO_EN_TRAMITE, // EN TRAMITE
                OffsetDateTime.now(),
                1,
                false
        );

        List<TesisAutor> autores = command.estudiantesIds().stream()
                .map(eId -> new TesisAutor(null, null, eId, OffsetDateTime.now(), 1, false))
                .toList();

        tesis.setAutores(autores);

        Tesis tesisGuardado = tesisRepositoryPort.guardar(tesis);

        String etapaCodigo = command.etapaTesis() != null ? command.etapaTesis() : TablaMaestraCodigos.ETAPA_TESIS_PROYECTO;
        RegistrarExpedienteCommand commandExpediente =  new RegistrarExpedienteCommand(
                tesisGuardado.getId(),
                command.numeroExpediente(),
                etapaCodigo, // PROYECTO DE TESIS
                command.numeroRevision() != null ? command.numeroRevision() : 1,
                command.fechaIngreso()
        );

        gestionarExpedienteUseCase.registrar(commandExpediente, files, false);

        Comision comision = new Comision(
                null,
                tesisGuardado.getId(),
                etapaCodigo,
                null,
                1, false
        );

        comisionRepositoryPort.guardar(comision);

        return tesisGuardado;
    }

    @Override
    public TesisDetalleView obtenerDetallePorId(UUID id) {
        log.info("Obteniendo una tesis a detalle por su id: {}", id);
        TesisDetalleView detalle = tesisRepositoryPort.obtenerDetallePorId(id);
        if (detalle == null) {
            throw new ResourceNotFoundException("Tesis", "id", id);
        }

        ExpedienteDetalleView expedienteActual = gestionarExpedienteUseCase.obtenerUltimoExpedienteTesisActivo(detalle.getId());
        detalle.setExpedienteActual(expedienteActual);

        return detalle;
    }

    @Override
    public TesisDetalleView obtenerPorId(UUID id) {
        log.info("Obteniendo una tesis por su id: {}", id);

        TesisDetalleView detalle = tesisRepositoryPort.obtenerDetallePorId(id);
        if (detalle == null) {
            throw new ResourceNotFoundException("Tesis", "id", id);
        }

        return detalle;
    }

    @Override
    public PageResult<TesisDetalleView> listarPorFiltros(TesisFiltro filtro, int page, int size) {
        log.info("Listando tesis por un filtro");

        PageResult<TesisDetalleView> pageResult = tesisRepositoryPort.listarPorFiltros(filtro, page, size);

        if (pageResult.getContent() != null && !pageResult.getContent().isEmpty()) {

            List<UUID> tesisIds = pageResult.getContent().stream()
                    .map(TesisDetalleView::getId)
                    .toList();

            Map<UUID, ExpedienteDetalleView> ultimosExpedientes = gestionarExpedienteUseCase.obtenerUltimosExpedientes(tesisIds);

            pageResult.getContent().forEach(tesis ->
                    tesis.setExpedienteActual(ultimosExpedientes.get(tesis.getId()))
            );

        }

        return pageResult;
    }

    private void validarListaEstudiantes(List<Long> estudiantesIds) {
        if (estudiantesIds == null || estudiantesIds.isEmpty() || estudiantesIds.size() > 2) {
            throw new BusinessRuleException("La tesis debe tener mínimo 1 y máximo 2 estudiantes como autores.");
        }

        Set<Long> unicos = new HashSet<>(estudiantesIds);
        if (unicos.size() != estudiantesIds.size()) {
            throw new BusinessRuleException("No se permiten estudiantes duplicados en la lista de autores.");
        }

        for (Long estId : estudiantesIds) {
            if (!estudianteRepositoryPort.existePorId(estId)) {
                throw new ResourceNotFoundException("Estudiante", "id", estId);
            }
        }
    }

}
