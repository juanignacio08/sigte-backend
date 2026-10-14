package unsch.fcsa.sigte.application.usecase.tramites;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import unsch.fcsa.sigte.application.command.PageResult;
import unsch.fcsa.sigte.application.command.files.ArchivoSubida;
import unsch.fcsa.sigte.application.command.tramites.TesisCommand;
import unsch.fcsa.sigte.application.command.tramites.TesisFiltro;
import unsch.fcsa.sigte.application.port.in.tramites.GestionarTesisUseCase;
import unsch.fcsa.sigte.application.port.out.maestro.EscuelaProfesionalRepositoryPort;
import unsch.fcsa.sigte.application.port.out.personas.EstudianteRepositoryPort;
import unsch.fcsa.sigte.application.port.out.storage.StoragePort;
import unsch.fcsa.sigte.application.port.out.tramites.TesisRepositoryPort;
import unsch.fcsa.sigte.application.port.out.tramites.dto.TesisDetalleView;
import unsch.fcsa.sigte.domain.exception.BusinessRuleException;
import unsch.fcsa.sigte.domain.exception.DuplicateKeyException;
import unsch.fcsa.sigte.domain.exception.ResourceNotFoundException;
import unsch.fcsa.sigte.domain.model.tramites.ExpedienteArchivo;
import unsch.fcsa.sigte.domain.model.tramites.ExpedienteTesis;
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
    private final StoragePort storagePort;

    public GestionarTesisUseCaseImpl(TesisRepositoryPort tesisRepositoryPort,
                                     EscuelaProfesionalRepositoryPort escuelaProfesionalRepositoryPort,
                                     EstudianteRepositoryPort estudianteRepositoryPort,
                                     StoragePort storagePort) {
        this.tesisRepositoryPort = tesisRepositoryPort;
        this.escuelaProfesionalRepositoryPort = escuelaProfesionalRepositoryPort;
        this.estudianteRepositoryPort = estudianteRepositoryPort;
        this.storagePort = storagePort;
    }

    @Override
    @Transactional
    public Tesis registrarProyecto(TesisCommand command, List<ArchivoSubida> files) {
        log.info("Registrando un proyecto de tesis. [ TESIS : {} ]", command.titulo());

        if (!escuelaProfesionalRepositoryPort.existePorId(command.escuelaId())) {
            throw new ResourceNotFoundException("Escuela", "id", command.escuelaId());
        }

        validarListaEstudiantes(command.estudiantesIds());

        if (tesisRepositoryPort.existeExpedientePorNumero(command.numeroExpediente())) {
            throw new DuplicateKeyException("ExpedienteTesis", "numeroExpediente", command.numeroExpediente());
        }

        for (Long estudianteId : command.estudiantesIds()) {
            if (tesisRepositoryPort.estudianteTieneTesisActiva(estudianteId)) {
                throw new BusinessRuleException("El estudiante con ID " + estudianteId + " ya cuenta con una tesis activa.");
            }
        }

        if (files != null && !files.isEmpty()) {
            if (files.size() > 5) {
                throw new BusinessRuleException("No puede adjuntar más de 5 archivos PDF por expediente.");
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

        ExpedienteTesis expediente = new ExpedienteTesis(
                null,
                null,
                command.numeroExpediente(),
                command.etapaTesis() != null ? command.etapaTesis() : TablaMaestraCodigos.ETAPA_TESIS_PROYECTO, // PROYECTO DE TESIS
                command.numeroRevision() != null ? command.numeroRevision() : 1,
                OffsetDateTime.now(),
                1,
                false,
                null
        );

        List<ExpedienteArchivo> listaArchivos = new ArrayList<>();

        try {
            for (ArchivoSubida archivo : files) {
                String rutaPdf = storagePort.guardarArchivoPdf(
                        archivo,
                        "expedientes"
                );

                ExpedienteArchivo expedienteArchivo = new ExpedienteArchivo(
                        null, null,
                        archivo.nombreOriginal(),
                        rutaPdf,
                        archivo.tamanioBytes(),
                        1, false
                );

                listaArchivos.add(expedienteArchivo);
            }

            expediente.setArchivos(listaArchivos);
            tesis.setAutores(autores);
            tesis.setExpedientes(List.of(expediente));

            return tesisRepositoryPort.guardar(tesis);

        } catch (Exception ex) {
            log.error("Error al registrar la tesis. Revirtiendo archivos físicos subidos...", ex);
            for (ExpedienteArchivo ea : listaArchivos) {
                if (ea != null) {
                    storagePort.eliminarArchivo(ea.getArchivoUrl());
                }
            }
            throw ex;
        }

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

        return tesisRepositoryPort.listarPorFiltros(filtro, page, size);
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
