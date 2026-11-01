package unsch.fcsa.sigte.application.usecase.tramites;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import unsch.fcsa.sigte.application.command.tramites.GenerarMemoCommand;
import unsch.fcsa.sigte.application.port.in.tramites.GestionarComisionUseCase;
import unsch.fcsa.sigte.application.port.in.tramites.GestionarDocumentosTesisUseCase;
import unsch.fcsa.sigte.application.port.out.storage.DocumentoGeneradorPort;
import unsch.fcsa.sigte.application.port.out.tramites.*;
import unsch.fcsa.sigte.application.port.out.tramites.dto.*;
import unsch.fcsa.sigte.domain.exception.BusinessRuleException;
import unsch.fcsa.sigte.domain.exception.ResourceNotFoundException;
import unsch.fcsa.sigte.domain.model.tramites.DocumentoTesis;
import unsch.fcsa.sigte.domain.model.tramites.ExpedienteTesis;
import unsch.fcsa.sigte.domain.util.AsuntoMemoBuilder;
import unsch.fcsa.sigte.domain.util.TablaMaestraCodigos;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
public class GestionarDocumentosTesisUseCaseImpl implements GestionarDocumentosTesisUseCase {

    private final DocumentoTesisRepositoryPort documentoTesisRepositoryPort;
    private final DocumentoGeneradorPort documentoGeneradorPort;
    private final TesisRepositoryPort tesisRepositoryPort;
    private final ExpedienteRepositoryPort expedienteRepositoryPort;
    private final GestionarComisionUseCase gestionarComisionUseCase;

    public GestionarDocumentosTesisUseCaseImpl(
            DocumentoTesisRepositoryPort documentoTesisRepositoryPort,
            DocumentoGeneradorPort documentoGeneradorPort,
            TesisRepositoryPort tesisRepositoryPort,
            ExpedienteRepositoryPort expedienteRepositoryPort,
            GestionarComisionUseCase gestionarComisionUseCase) {
        this.documentoTesisRepositoryPort = documentoTesisRepositoryPort;
        this.documentoGeneradorPort = documentoGeneradorPort;
        this.tesisRepositoryPort = tesisRepositoryPort;
        this.expedienteRepositoryPort = expedienteRepositoryPort;
        this.gestionarComisionUseCase = gestionarComisionUseCase;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoTesisDetalleView> listarPorExpediente(Long expedienteId) {
        log.info("Listando documentos del expediente. [ EXPEDIENTE : {} ]", expedienteId);
        return documentoTesisRepositoryPort.listarPorExpediente(expedienteId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoTesisDetalleView> listarPorTesis(UUID tesisId) {
        log.info("Listando documentos de la tesis. [ TESIS : {} ]", tesisId);
        return documentoTesisRepositoryPort.listarPorTesis(tesisId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoTesisDetalleView> listarPorExpedienteYTipo(Long expedienteId, String tipoDocumentoCodigo) {
        log.info("Listando documentos del expediente por tipo. [ EXPEDIENTE : {} | TIPO : {} ]",
                expedienteId, tipoDocumentoCodigo);
        return documentoTesisRepositoryPort.listarPorExpedienteYTipo(expedienteId, tipoDocumentoCodigo);
    }

    @Override
    public DocumentoTesis obtenerPorId(Long id) {
        log.info("Obteniendo documento por id. [ DOCUMENTO : {} ]", id);
        return documentoTesisRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "DocumentoTesis", "id", id.toString()));
    }

    @Override
    @Transactional
    public DocumentoTesis generarMemo(GenerarMemoCommand command) {
        log.info("Generando memo. [ TESIS : {} | EXPEDIENTE : {} | N° : {}-{} ]",
                command.tesisId(), command.expedienteId(),
                command.numeroCorrelativo(), command.anio());

        TesisDetalleView tesis = tesisRepositoryPort.obtenerDetallePorId(command.tesisId());

        ExpedienteTesis expediente = expedienteRepositoryPort.buscarPorId(command.expedienteId())
                .orElseThrow(() -> new ResourceNotFoundException("ExpedienteTesis", "id", command.expedienteId().toString()));

        if (!expediente.getTesisId().equals(command.tesisId())) {
            throw new BusinessRuleException("El expediente no pertenece a la tesis indicada.");
        }

        ComisionDetalleView comision = gestionarComisionUseCase.obtenerPorTesisYEtapa(tesis.getId(), expediente.getEtapaCodigo());

        String tipoDocumentoCodigo = mapearTipoDocumento(expediente.getEtapaCodigo());

        if (documentoTesisRepositoryPort.existeCorrelativo(tipoDocumentoCodigo, command.anio(), command.numeroCorrelativo())) {
            throw new BusinessRuleException("Ya existe un documento con el número " + command.numeroCorrelativo() + "-" + command.anio() + ". Por favor elija otro.");
        }

        String rutaPlantilla = tesis.getPlantillaMemo();

        if (rutaPlantilla == null) { throw new BusinessRuleException("NO existe una plantilla de memorando en la escuela " + tesis.getEscuelaDenominacion()); }

        if (comision.getMiembros() == null || comision.getMiembros().isEmpty()) {
            throw new BusinessRuleException("La comisión del expediente no tiene miembros registrados.");
        }

        List<MemorandumData.MiembroMemo> miembros = comision.getMiembros().stream()
                .map(m -> new MemorandumData.MiembroMemo(m.docenteNombre(), m.rolDenominacion()))
                .toList();

        String asunto = AsuntoMemoBuilder.construir(expediente.getEtapaCodigo(), expediente.getNumeroRevision());
        String tesistas = construirTesistas(tesis);
        String tipoDocumento = mapearNombreTipoDocumento(expediente.getEtapaCodigo());

        MemorandumData data = new MemorandumData(
                String.format("%03d",command.numeroCorrelativo()),
                String.valueOf(command.anio()),
                miembros,
                asunto,
                tesistas,
                expediente.getNumeroExpediente(),
                LocalDate.now(),
                tipoDocumento,
                tesis.getTitulo(),
                rutaPlantilla
        );

        DocumentoGenerado docGenerado = documentoGeneradorPort.generarMemorandum(data);

        DocumentoTesis nuevo = new DocumentoTesis(
                null,
                command.tesisId(),
                command.expedienteId(),
                tipoDocumentoCodigo,
                command.numeroCorrelativo(),
                command.anio(),
                LocalDate.now(),
                docGenerado.rutaWord(),
                docGenerado.rutaPdf(),
                docGenerado.tamanioWordBytes(),
                docGenerado.tamanioPdfBytes(),
                null, null,
                TablaMaestraCodigos.ESTADO_DOCUMENTO_ESTADO_GENERADO,
                1,
                false
        );

        DocumentoTesis guardado = documentoTesisRepositoryPort.guardar(nuevo);

        expediente.marcarComoMemoGenerado();
        expedienteRepositoryPort.guardar(expediente);

        log.info("Memo {} generado y guardado. ID documento: {}", command.numeroCorrelativo(), guardado.getId());

        return guardado;
    }

    private String mapearTipoDocumento(String etapaCodigo) {
        return switch (etapaCodigo) {
            case TablaMaestraCodigos.ETAPA_TESIS_PROYECTO -> TablaMaestraCodigos.TIPO_DOCUMENTO_MEMO_PROYECTO;
            case TablaMaestraCodigos.ETAPA_TESIS_BORRADOR -> TablaMaestraCodigos.TIPO_DOCUMENTO_MEMO_BORRADOR;
            default -> throw new BusinessRuleException("No se puede generar memo para la etapa: " + etapaCodigo);
        };
    }

    private String mapearNombreTipoDocumento(String etapaCodigo) {
        return switch (etapaCodigo) {
            case TablaMaestraCodigos.ETAPA_TESIS_PROYECTO -> "Proyecto";
            case TablaMaestraCodigos.ETAPA_TESIS_BORRADOR  -> "Borrador";
            default -> "Documento";
        };
    }

    private String construirTesistas(TesisDetalleView tesis) {
        if (tesis.getAutores() == null || tesis.getAutores().isEmpty()) {
            throw new BusinessRuleException("La tesis no tiene autores registrados.");
        }

        List<String> nombres = tesis.getAutores().stream()
                .map(a -> String.format("%s %s %s",
                        a.nombres(),
                        a.primerApellido(),
                        a.segundoApellido()).trim())
                .toList();

        if (nombres.size() == 1) return nombres.getFirst();
        return String.join(" y ", nombres);
    }

}
