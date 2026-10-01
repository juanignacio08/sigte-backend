package unsch.fcsa.sigte.application.usecase.personas;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import unsch.fcsa.sigte.application.command.personas.DocenteCommand;
import unsch.fcsa.sigte.application.port.in.personas.GestionarDocenteUseCase;
import unsch.fcsa.sigte.application.port.out.maestro.DepartamentoRepositoryPort;
import unsch.fcsa.sigte.application.port.out.maestro.EscuelaProfesionalRepositoryPort;
import unsch.fcsa.sigte.application.port.out.maestro.MaestroRepositoryPort;
import unsch.fcsa.sigte.application.port.out.personas.DocenteRepositoryPort;
import unsch.fcsa.sigte.domain.exception.BusinessRuleException;
import unsch.fcsa.sigte.domain.exception.DuplicateKeyException;
import unsch.fcsa.sigte.domain.exception.ResourceNotFoundException;
import unsch.fcsa.sigte.domain.model.personas.Docente;

@Slf4j
public class GestionarDocenteUseCaseImpl implements GestionarDocenteUseCase {

    private final DocenteRepositoryPort docenteRepositoryPort;
    private final DepartamentoRepositoryPort departamentoRepositoryPort;
    private final EscuelaProfesionalRepositoryPort escuelaProfesionalRepositoryPort;
    private final MaestroRepositoryPort maestroRepositoryPort;

    public GestionarDocenteUseCaseImpl(
            DocenteRepositoryPort docenteRepositoryPort,
            DepartamentoRepositoryPort departamentoRepositoryPort,
            EscuelaProfesionalRepositoryPort escuelaProfesionalRepositoryPort,
            MaestroRepositoryPort maestroRepositoryPort
    ) {
        this.docenteRepositoryPort = docenteRepositoryPort;
        this.departamentoRepositoryPort = departamentoRepositoryPort;
        this.escuelaProfesionalRepositoryPort = escuelaProfesionalRepositoryPort;
        this.maestroRepositoryPort = maestroRepositoryPort;
    }

    @Override
    @Transactional
    public Docente guardar(DocenteCommand command) {
        log.info("Registrando un docente. [ DOCENTE : {} ]", command.nombres());

        validarCodigosMaestros(command.tipoDocumentoCodigo(), command.generoCodigo());

        if (docenteRepositoryPort.existePorCorreoInstitucional(command.correoInstitucional())) {
            throw new DuplicateKeyException("Docente", "correoInstitucional", command.correoInstitucional());
        }

        if (command.tipoDocumentoCodigo() != null && command.numeroDocumento() != null) {
            if (docenteRepositoryPort.existePorDocumento(command.tipoDocumentoCodigo(), command.numeroDocumento())) {
                throw new DuplicateKeyException("Docente", "documento", command.tipoDocumentoCodigo() + "-" + command.numeroDocumento());
            }
        }

        if (!departamentoRepositoryPort.existePorId(command.departamentoAcademicoId())) {
            throw new ResourceNotFoundException("Departamento",  "id", command.departamentoAcademicoId());
        }

        if (!escuelaProfesionalRepositoryPort.existePorId(command.escuelaId())) {
            throw new ResourceNotFoundException("Escuela",  "id", command.escuelaId());
        }

        Docente nuevoDocente = new Docente(
                command.codigo(),
                command.escuelaId(),
                command.departamentoAcademicoId(),
                command.tipoDocumentoCodigo(),
                command.numeroDocumento(),
                command.nombres(),
                command.primerApellido(),
                command.segundoApellido(),
                command.correoInstitucional(),
                command.generoCodigo(),
                command.telefono()
        );

        return docenteRepositoryPort.guardar(nuevoDocente);
    }

    @Override
    public Docente actualizar(DocenteCommand command) {
        if (command.id() == null) {
            throw new BusinessRuleException("El ID del docente es obligatorio para actualizar.");
        }

        log.info("Actualizando los datos de un docente. [ DOCENTE : {} ]", command.nombres());

        Docente docente = obtenerPorId(command.id());

        validarCodigosMaestros(command.tipoDocumentoCodigo(), command.generoCodigo());

        if (!docente.getCorreoInstitucional().equalsIgnoreCase(command.correoInstitucional())
                && docenteRepositoryPort.existePorCorreoInstitucional(command.correoInstitucional())) {
            throw new DuplicateKeyException("Docente", "correoInstitucional", command.correoInstitucional());
        }

        if (command.tipoDocumentoCodigo() != null && command.numeroDocumento() != null) {
            if ((!docente.getTipoDocumentoCodigo().equals(command.tipoDocumentoCodigo()) ||
                    !docente.getNumeroDocumento().equals(command.numeroDocumento())) &&
                    docenteRepositoryPort.existePorDocumento(command.tipoDocumentoCodigo(), command.numeroDocumento())) {
                throw new DuplicateKeyException("Docente", "documento", command.tipoDocumentoCodigo() + "-" + command.numeroDocumento());
            }
        }

        if (!docente.getDepartamentoAcademicoId().equals(command.departamentoAcademicoId()) &&
                !departamentoRepositoryPort.existePorId(command.departamentoAcademicoId())) {
            throw new ResourceNotFoundException("Departamento",  "id", command.departamentoAcademicoId());
        }

        if (!docente.getEscuelaId().equals(command.escuelaId()) &&
                !escuelaProfesionalRepositoryPort.existePorId(command.escuelaId())) {
            throw new ResourceNotFoundException("Escuela",  "id", command.escuelaId());
        }

        docente.actualizarDatos(
                command.codigo(),
                command.escuelaId(),
                command.departamentoAcademicoId(),
                command.tipoDocumentoCodigo(),
                command.numeroDocumento(),
                command.nombres(),
                command.primerApellido(),
                command.segundoApellido(),
                command.correoInstitucional(),
                command.generoCodigo(),
                command.telefono()
        );

        return docenteRepositoryPort.guardar(docente);
    }

    @Override
    public void activar(Long id) {
        Docente docente = docenteRepositoryPort.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Docente", "id", id));

        if (docente.getEstado() == 0) {
            docente.activar();
            docenteRepositoryPort.guardar(docente);
        }
    }

    @Override
    public void desactivar(Long id) {
        Docente docente = docenteRepositoryPort.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Docente", "id", id));

        if (docente.getEstado() == 1) {
            docente.desactivar();
            docenteRepositoryPort.guardar(docente);
        }
    }

    @Override
    public void eliminar(Long id) {
        Docente docente = docenteRepositoryPort.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Docente", "id", id));

        if (!docente.isEliminado()) {
            docente.marcarComoEliminado();
            docenteRepositoryPort.guardar(docente);
        }
    }

    @Override
    public Docente obtenerPorId(Long id) {
        if (id == null || id <= 0) { throw new BusinessRuleException("ID incorrecto. [ DOCENTE ]."); }

        return docenteRepositoryPort.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Docente", "id", id));
    }

    private void validarCodigosMaestros(String tipoDocumentoCodigo, String generoCodigo) {
        if (tipoDocumentoCodigo != null && !maestroRepositoryPort.existsByCode(tipoDocumentoCodigo)) {
            throw new ResourceNotFoundException("TablaMaestra", "codigo", tipoDocumentoCodigo);
        }
        if (generoCodigo != null && !maestroRepositoryPort.existsByCode(generoCodigo)) {
            throw new ResourceNotFoundException("TablaMaestra", "codigo", generoCodigo);
        }
    }
}
