package unsch.fcsa.sigte.application.usecase.personas;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import unsch.fcsa.sigte.application.command.PageResult;
import unsch.fcsa.sigte.application.command.personas.EstudianteCommand;
import unsch.fcsa.sigte.application.port.in.personas.GestionarEstudianteUseCase;
import unsch.fcsa.sigte.application.port.out.maestro.EscuelaProfesionalRepositoryPort;
import unsch.fcsa.sigte.application.port.out.personas.EstudianteRepositoryPort;
import unsch.fcsa.sigte.application.port.out.personas.dto.EstudianteDetalleView;
import unsch.fcsa.sigte.application.port.out.personas.dto.EstudianteFiltro;
import unsch.fcsa.sigte.domain.exception.BusinessRuleException;
import unsch.fcsa.sigte.domain.exception.DuplicateKeyException;
import unsch.fcsa.sigte.domain.exception.ResourceNotFoundException;
import unsch.fcsa.sigte.domain.model.personas.Estudiante;

@Slf4j
public class GestionarEstudianteUseCaseImpl implements GestionarEstudianteUseCase {

    private final EstudianteRepositoryPort estudianteRepositoryPort;
    private final EscuelaProfesionalRepositoryPort escuelaProfesionalRepositoryPort;

    public GestionarEstudianteUseCaseImpl(EstudianteRepositoryPort estudianteRepositoryPort,
                                          EscuelaProfesionalRepositoryPort escuelaProfesionalRepositoryPort) {
        this.estudianteRepositoryPort = estudianteRepositoryPort;
        this.escuelaProfesionalRepositoryPort = escuelaProfesionalRepositoryPort;
    }

    @Override
    @Transactional
    public Estudiante guardar(EstudianteCommand command) {
        log.info("Registrando a un nuevo estudiante");

        if (!escuelaProfesionalRepositoryPort.existePorId(command.escuelaId())) {
            throw new ResourceNotFoundException("Escuela", "id", command.escuelaId());
        }

        if (command.codigo() != null && !command.codigo().isBlank()
                && estudianteRepositoryPort.existePorCodigo(command.codigo())) {
            throw new DuplicateKeyException("Estudiante", "codigo", command.codigo());
        }

        if (command.numeroDocumento() != null && !command.numeroDocumento().isBlank()
                && estudianteRepositoryPort.existePorNumeroDocumento(command.numeroDocumento())) {
            throw new DuplicateKeyException("Estudiante", "numeroDocumento", command.numeroDocumento());
        }

        if (estudianteRepositoryPort.existePorCorreoInstitucional(command.correoInstitucional())) {
            throw new DuplicateKeyException("Estudiante", "correoInstitucional", command.correoInstitucional());
        }

        Estudiante estudiante = new Estudiante(
                command.escuelaId(),
                command.codigo(),
                command.numeroDocumento(),
                command.nombres(),
                command.primerApellido(),
                command.segundoApellido(),
                command.correoInstitucional(),
                command.generoCodigo(),
                command.telefono()
        );

        return estudianteRepositoryPort.guardar(estudiante);
    }

    @Override
    @Transactional
    public Estudiante actualizar(EstudianteCommand command) {
        if (command.id() == null) {
            throw new BusinessRuleException("El ID del estudiante es obligatorio para actualizar.");
        }

        log.info("Actualizando a un estudiante [ ESTUDIANTE : {} ]", command.id());

        Estudiante estudiante = estudianteRepositoryPort.obtenerPorId(command.id())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante", "id", command.id()));

        if (!estudiante.getEscuelaId().equals(command.escuelaId()) &&
                !escuelaProfesionalRepositoryPort.existePorId(command.escuelaId())) {
            throw new ResourceNotFoundException("Escuela",  "id", command.escuelaId());
        }

        if (command.codigo() != null && !command.codigo().isBlank()
                && (estudiante.getCodigo() == null || !estudiante.getCodigo().equalsIgnoreCase(command.codigo()))
                && estudianteRepositoryPort.existePorCodigo(command.codigo())) {
            throw new DuplicateKeyException("Estudiante", "codigo", command.codigo());
        }

        if (command.numeroDocumento() != null && !command.numeroDocumento().isBlank()
                && (estudiante.getNumeroDocumento() == null || !estudiante.getNumeroDocumento().equalsIgnoreCase(command.numeroDocumento()))
                && estudianteRepositoryPort.existePorNumeroDocumento(command.numeroDocumento())) {
            throw new DuplicateKeyException("Estudiante", "numeroDocumento", command.numeroDocumento());
        }

        if (!estudiante.getCorreoInstitucional().equalsIgnoreCase(command.correoInstitucional())
                && estudianteRepositoryPort.existePorCorreoInstitucional(command.correoInstitucional())) {
            throw new DuplicateKeyException("Estudiante", "correoInstitucional", command.correoInstitucional());
        }

        estudiante.actualizarDatos(
                command.escuelaId(),
                command.codigo(),
                command.numeroDocumento(),
                command.nombres(),
                command.primerApellido(),
                command.segundoApellido(),
                command.correoInstitucional(),
                command.generoCodigo(),
                command.telefono()
        );

        return estudianteRepositoryPort.guardar(estudiante);
    }

    @Override
    public Estudiante obtenerPorId(Long id) {
        return estudianteRepositoryPort.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante", "id", id));
    }

    @Override
    public EstudianteDetalleView obtenerDetallePorId(Long id) {
        log.info("Obteniendo el estudiante por id [{}]", id);

        EstudianteDetalleView detalle = estudianteRepositoryPort.obtenerDetallePorId(id);
        if (detalle == null) {
            throw new ResourceNotFoundException("Estudiante", "id", id);
        }
        return detalle;
    }

    @Override
    public PageResult<EstudianteDetalleView> listarPorFiltros(EstudianteFiltro filtro, int page, int size) {
        log.info("Busqueda de estudiantes por filtros.");
        return estudianteRepositoryPort.listarPorFiltros(filtro, page, size);
    }

    @Override
    @Transactional
    public Estudiante activar(Long id) {
        log.info("Activar estudiante [{}]", id);

        Estudiante estudiante = obtenerPorId(id);
        if (estudiante.getEstado().equals(1)) {
            return estudiante;
        }

        estudiante.activar();
        return estudianteRepositoryPort.guardar(estudiante);
    }

    @Override
    @Transactional
    public Estudiante desactivar(Long id) {
        log.info("Desactivar estudiante [{}]", id);

        Estudiante estudiante = obtenerPorId(id);
        if (estudiante.getEstado().equals(0)) {
            return estudiante;
        }

        estudiante.desactivar();
        return estudianteRepositoryPort.guardar(estudiante);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminar estudiante [{}]", id);

        Estudiante estudiante = obtenerPorId(id);
        estudiante.eliminar();
        estudianteRepositoryPort.guardar(estudiante);
    }

}