package unsch.fcsa.sigte.application.usecase.tramites;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;
import unsch.fcsa.sigte.application.command.tramites.GenerarMemoCommand;
import unsch.fcsa.sigte.application.command.tramites.RegistrarDictamenCommand;
import unsch.fcsa.sigte.application.port.in.tramites.GestionarComisionUseCase;
import unsch.fcsa.sigte.application.port.in.tramites.GestionarDocumentosTesisUseCase;
import unsch.fcsa.sigte.application.port.out.notificaciones.EmailPort;
import unsch.fcsa.sigte.application.port.out.storage.DocumentoGeneradorPort;
import unsch.fcsa.sigte.application.port.out.storage.StoragePort;
import unsch.fcsa.sigte.application.port.out.tramites.*;
import unsch.fcsa.sigte.application.port.out.tramites.dto.*;
import unsch.fcsa.sigte.domain.exception.BusinessRuleException;
import unsch.fcsa.sigte.domain.exception.ResourceNotFoundException;
import unsch.fcsa.sigte.domain.model.tramites.DocumentoTesis;
import unsch.fcsa.sigte.domain.model.tramites.EnvioCorreo;
import unsch.fcsa.sigte.domain.model.tramites.ExpedienteArchivo;
import unsch.fcsa.sigte.domain.model.tramites.ExpedienteTesis;
import unsch.fcsa.sigte.domain.util.AsuntoMemoBuilder;
import unsch.fcsa.sigte.domain.util.TablaMaestraCodigos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class GestionarDocumentosTesisUseCaseImpl implements GestionarDocumentosTesisUseCase {

    private final DocumentoTesisRepositoryPort documentoTesisRepositoryPort;
    private final DocumentoGeneradorPort documentoGeneradorPort;
    private final TesisRepositoryPort tesisRepositoryPort;
    private final ExpedienteRepositoryPort expedienteRepositoryPort;
    private final ExpedienteArchivoRepositoryPort expedienteArchivoRepositoryPort;
    private final EmailPort emailPort;
    private final EnvioCorreoRepositoryPort envioCorreoRepositoryPort;
    private final GestionarComisionUseCase gestionarComisionUseCase;
    private final StoragePort storagePort;
    private final ObjectMapper objectMapper;

    public GestionarDocumentosTesisUseCaseImpl(
            DocumentoTesisRepositoryPort documentoTesisRepositoryPort,
            DocumentoGeneradorPort documentoGeneradorPort,
            TesisRepositoryPort tesisRepositoryPort,
            ExpedienteRepositoryPort expedienteRepositoryPort,
            ExpedienteArchivoRepositoryPort expedienteArchivoRepositoryPort,
            EmailPort emailPort,
            EnvioCorreoRepositoryPort envioCorreoRepositoryPort,
            GestionarComisionUseCase gestionarComisionUseCase,
            StoragePort storagePort,
            ObjectMapper objectMapper) {
        this.documentoTesisRepositoryPort = documentoTesisRepositoryPort;
        this.documentoGeneradorPort = documentoGeneradorPort;
        this.tesisRepositoryPort = tesisRepositoryPort;
        this.expedienteRepositoryPort = expedienteRepositoryPort;
        this.expedienteArchivoRepositoryPort = expedienteArchivoRepositoryPort;
        this.emailPort = emailPort;
        this.envioCorreoRepositoryPort = envioCorreoRepositoryPort;
        this.gestionarComisionUseCase = gestionarComisionUseCase;
        this.storagePort = storagePort;
        this.objectMapper = objectMapper;
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

        if (documentoTesisRepositoryPort.existeMemoPorExpediente(command.expedienteId())) {
            throw new BusinessRuleException("No se puede crear un memorando. Ya existe un memorando generado para este expediente.");
        }

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

    @Override
    @Transactional
    public DocumentoTesis volverAGenerar(Long documentoId) {
        log.info("Volviendo a generar DocumentoTesis. [ DOCUMENTO : {} ]", documentoId.toString());

        DocumentoTesis documentoTesis = documentoTesisRepositoryPort.buscarPorId(documentoId)
                .orElseThrow(() -> new ResourceNotFoundException("Documento", "id", documentoId));

        TesisDetalleView tesis = tesisRepositoryPort.obtenerDetallePorId(documentoTesis.getTesisId());

        ExpedienteTesis expediente = expedienteRepositoryPort.buscarPorId(documentoTesis.getExpedienteTesisId())
                .orElseThrow(() -> new ResourceNotFoundException("ExpedienteTesis", "id", documentoTesis.getExpedienteTesisId().toString()));

        if (!expediente.getTesisId().equals(tesis.getId())) {
            throw new BusinessRuleException("El expediente no pertenece a la tesis indicada.");
        }

        ComisionDetalleView comision = gestionarComisionUseCase.obtenerPorTesisYEtapa(tesis.getId(), expediente.getEtapaCodigo());

        String tipoDocumentoCodigo = mapearTipoDocumento(expediente.getEtapaCodigo());

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
                String.format("%03d", documentoTesis.getNumeroCorrelativo()),
                String.valueOf(documentoTesis.getAnioCorrelativo()),
                miembros,
                asunto,
                tesistas,
                expediente.getNumeroExpediente(),
                LocalDate.now(),
                tipoDocumento,
                tesis.getTitulo(),
                rutaPlantilla
        );

        String rutaWordAntiguo = documentoTesis.getArchivoWordUrl();
        String rutaPdfAntiguo = documentoTesis.getArchivoPdfUrl();

        DocumentoGenerado docGenerado = documentoGeneradorPort.generarMemorandum(data);

        String nuevaRutaWord = docGenerado.rutaWord();
        String nuevaRutaPdf  = docGenerado.rutaPdf();

        documentoTesis.volverAGenerar(
                tipoDocumentoCodigo, docGenerado.rutaWord(), docGenerado.rutaPdf(), docGenerado.tamanioWordBytes(), docGenerado.tamanioPdfBytes()
        );

        DocumentoTesis guardado = documentoTesisRepositoryPort.guardar(documentoTesis);

        expediente.marcarComoMemoGenerado();
        expedienteRepositoryPort.guardar(expediente);

        List<String> rutasViejas = new ArrayList<>();
        if (rutaWordAntiguo != null && !rutaWordAntiguo.equals(nuevaRutaWord)) {
            rutasViejas.add(rutaWordAntiguo);
        }
        if (rutaPdfAntiguo != null && !rutaPdfAntiguo.equals(nuevaRutaPdf)) {
            rutasViejas.add(rutaPdfAntiguo);
        }

        if (!rutasViejas.isEmpty()) {
            documentoGeneradorPort.eliminarArchivosGenerados(rutasViejas);
        }

        return guardado;
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentoTesisDetalleView obtenerMemoPorExpediente(Long expedienteId) {
        log.info("Buscando el memo del expediente. [ EXPEDIENTE : {} ]", expedienteId);
        return documentoTesisRepositoryPort.obtenerMemoPorExpediente(expedienteId);
    }

    @Override
    @Transactional
    public void eliminarMemo(Long documentoId) {
        log.info("Eliminando memorando físicamente. [ DOCUMENTO : {} ]", documentoId);

        DocumentoTesis doc = documentoTesisRepositoryPort.buscarPorId(documentoId)
                .orElseThrow(() -> new ResourceNotFoundException("DocumentoTesis", "id", documentoId.toString()));

        if (esTipoMemo(doc.getTipoDocumentoCodigo()) && doc.getExpedienteTesisId() != null) {
            ExpedienteTesis expediente = expedienteRepositoryPort
                    .buscarPorId(doc.getExpedienteTesisId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "ExpedienteTesis", "id", doc.getExpedienteTesisId().toString()));

            expediente.revertirMemoGenerado();
            expedienteRepositoryPort.guardar(expediente);

            log.info("Expediente {} revertido a REGISTRADO", expediente.getId());

            if (doc.getArchivoWordUrl() != null) {
                storagePort.eliminarTrasCommit(doc.getArchivoWordUrl());
            }
            if (doc.getArchivoPdfUrl() != null) {
                storagePort.eliminarTrasCommit(doc.getArchivoPdfUrl());
            }

            documentoTesisRepositoryPort.eliminar(documentoId);

            log.info("Documento {} eliminado correctamente", documentoId);
        }
    }

    @Override
    @Transactional
    public DocumentoTesis registrarDictamen(RegistrarDictamenCommand command) {
        log.info("Registrando dictamen. [ EXPEDIENTE : {} | RESULTADO : {} ]",
                command.expedienteId(), command.resultadoCodigo());

        ExpedienteTesis expediente = expedienteRepositoryPort.buscarPorId(command.expedienteId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "ExpedienteTesis", "id", command.expedienteId().toString()));

        if (!TablaMaestraCodigos.ESTADO_EXPEDIENTE_MEMO_ENVIADO.equals(expediente.getEstadoCodigo())) {
            throw new BusinessRuleException(
                    "Solo se puede registrar dictamen cuando el memo ya fue enviado a la comisión.");
        }

        if (documentoTesisRepositoryPort.existeDictamenPorExpediente(command.expedienteId())) {
            throw new BusinessRuleException(
                    "Este expediente ya tiene un dictamen registrado. Elimine el anterior antes de registrar uno nuevo.");
        }

        String rutaWord = null, rutaPdf = null;
        Long tamanioWord = null, tamanioPdf = null;
        List<String> rutasSubidas = new ArrayList<>();

        try {
            if (command.archivoWord() != null) {
                rutaWord = storagePort.guardarArchivo(command.archivoWord(), "documentos/dictamenes");
                tamanioWord = command.archivoWord().tamanioBytes();
                rutasSubidas.add(rutaWord);
            }

            if (command.archivoPdf() != null) {
                rutaPdf = storagePort.guardarArchivoPdf(command.archivoPdf(), "documentos/dictamenes");
                tamanioPdf = command.archivoPdf().tamanioBytes();
                rutasSubidas.add(rutaPdf);
            }

            LocalDate fechaEmision = command.fechaEmision() != null
                    ? command.fechaEmision()
                    : LocalDate.now();

            DocumentoTesis dictamen = new DocumentoTesis(
                    null,
                    expediente.getTesisId(),
                    command.expedienteId(),
                    TablaMaestraCodigos.TIPO_DOCUMENTO_DICTAMEN,
                    null,
                    null,
                    fechaEmision,
                    rutaWord,
                    rutaPdf,
                    tamanioWord,
                    tamanioPdf,
                    command.resultadoCodigo(),
                    command.observaciones(),
                    TablaMaestraCodigos.ESTADO_DOCUMENTO_ESTADO_RECIBIDO,
                    1,
                    false
            );

            DocumentoTesis guardado = documentoTesisRepositoryPort.guardar(dictamen);

            if (TablaMaestraCodigos.RESULTADO_DICTAMEN_APROBADO.equals(command.resultadoCodigo())) {
                expediente.marcarComoDictaminadoAprobado();
            } else {
                expediente.marcarComoDictaminadoObservado();
            }
            expedienteRepositoryPort.guardar(expediente);

            log.info("Dictamen registrado. [ DOCUMENTO : {} | EXPEDIENTE : {} | RESULTADO : {} ]",
                    guardado.getId(), expediente.getId(), command.resultadoCodigo());

            return guardado;

        } catch (Exception e) {
            // Rollback inmediato de archivos subidos (aún no referenciados en BD)
            for (String ruta : rutasSubidas) {
                try {
                    storagePort.eliminarArchivo(ruta);
                } catch (Exception cleanupError) {
                    log.warn("No se pudo limpiar el archivo huérfano: {}", ruta, cleanupError);
                }
            }
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentoTesisDetalleView obtenerDictamenPorExpediente(Long expedienteId) {
        log.info("Buscando el dictamen del expediente. [ EXPEDIENTE : {} ]", expedienteId);
        return documentoTesisRepositoryPort.obtenerDictamenPorExpediente(expedienteId);
    }

    @Override
    @Transactional
    public void eliminarDictamen(Long expedienteId) {
        log.info("Eliminando dictamen del expediente. [ EXPEDIENTE : {} ]", expedienteId);

        ExpedienteTesis expediente = expedienteRepositoryPort.buscarPorId(expedienteId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "ExpedienteTesis", "id", expedienteId.toString()));

        DocumentoTesisDetalleView dictamen = documentoTesisRepositoryPort.obtenerDictamenPorExpediente(expedienteId);

        if (documentoTesisRepositoryPort.existeResolucionPorExpediente(expedienteId)) {
            throw new BusinessRuleException(
                    "No se puede eliminar el dictamen porque ya existe una resolución generada. " +
                            "Elimine la resolución primero.");
        }

        expediente.revertirDictamen();
        expedienteRepositoryPort.guardar(expediente);

        if (dictamen.getArchivoWordUrl() != null) {
            storagePort.eliminarTrasCommit(dictamen.getArchivoWordUrl());
        }
        if (dictamen.getArchivoPdfUrl() != null) {
            storagePort.eliminarTrasCommit(dictamen.getArchivoPdfUrl());
        }

        documentoTesisRepositoryPort.eliminar(dictamen.getId());

        log.info("Dictamen eliminado y expediente revertido a MEMO_ENVIADO. [ EXPEDIENTE : {} ]",
                expedienteId);

    }

    @Override
    @Transactional
    public void enviarMemo(Long documentoId) {
        log.info("Enviando memo. [ DOCUMENTO : {} ]", documentoId);

        // 1. Cargar documento
        DocumentoTesis doc = documentoTesisRepositoryPort.buscarPorId(documentoId)
                .orElseThrow(() -> new ResourceNotFoundException("DocumentoTesis", "id", documentoId.toString()));

        if (!TablaMaestraCodigos.ESTADO_DOCUMENTO_ESTADO_GENERADO.equals(doc.getEstadoCodigo())) {
            throw new BusinessRuleException("El documento no está en estado GENERADO. Solo se pueden enviar memos recién generados.");
        }

        // 2. Cargar expediente
        ExpedienteTesis expediente = expedienteRepositoryPort.buscarPorId(doc.getExpedienteTesisId())
                .orElseThrow(() -> new ResourceNotFoundException("ExpedienteTesis", "id", doc.getExpedienteTesisId().toString()));

        // 3. Cargar comisión + tesis con autores
        ComisionDetalleView comision = gestionarComisionUseCase
                .obtenerPorTesisYEtapa(expediente.getTesisId(), expediente.getEtapaCodigo());

        TesisDetalleView tesis = tesisRepositoryPort.obtenerDetallePorId(expediente.getTesisId());

        // 4. Construir lista de destinatarios (miembros + autores)
        List<Map<String, String>> destinatarios = new ArrayList<>();

        if (comision.getMiembros() != null) {
            for (ComisionMiembroDetalleView m : comision.getMiembros()) {
                if (m.correoInstitucional() == null || m.correoInstitucional().isBlank()) continue;
                destinatarios.add(Map.of(
                        "email", m.correoInstitucional(),
                        "nombre", m.docenteNombre(),
                        "rol",    m.rolDenominacion()
                ));
            }
        }

        if (tesis.getAutores() != null) {
            for (AutorView autor : tesis.getAutores()) {
                String correo = autor.correoInstitucional();
                if (correo == null || correo.isBlank()) continue;

                String nombreCompleto = String.format("%s %s %s",
                        autor.nombres(),
                        autor.primerApellido(),
                        autor.segundoApellido() != null ? autor.segundoApellido() : ""
                ).trim();

                destinatarios.add(Map.of(
                        "email", correo,
                        "nombre", nombreCompleto,
                        "rol",    "Autor"
                ));
            }
        }

        if (destinatarios.isEmpty()) {
            throw new BusinessRuleException("No hay destinatarios con correo registrado (ni comisión ni autores).");
        }

        // 5. Construir lista de adjuntos (PDF memo + PDFs expediente)
        List<String> adjuntos = new ArrayList<>();
        if (doc.getArchivoPdfUrl() != null) {
            adjuntos.add(doc.getArchivoPdfUrl());
        }
        for (ExpedienteArchivo arch : expedienteArchivoRepositoryPort.obtenerPorExpediente(expediente.getId())) {
            if (arch.getArchivoUrl() != null && !arch.getArchivoUrl().isBlank()) {
                adjuntos.add(arch.getArchivoUrl());
            }
        }

        // 6. Armar asunto y cuerpo
        String tipoMemo = mapearNombreTipoDocumento(expediente.getEtapaCodigo());
        String asunto = "Revision de " + tipoMemo + " de Tesis";
        String nombreMemo = "Memorando Múltiple N° " + String.format("%03d", doc.getNumeroCorrelativo()) + "-" + doc.getAnioCorrelativo() + "-UNSCH-FCSA-D";
        String cuerpo = """
            Estimados miembros de la comisión y autores:

            Se adjunta el %s referido a la revisión y aprobación
            de la tesis del expediente N° %s.

            Además, se adjuntan los documentos enviados por el tesista en su expediente.

            Atentamente,
            Secretaría de Facultad - FCSA UNSCH
            """.formatted(
                nombreMemo,
                expediente.getNumeroExpediente()
        );

        //7. Serializar JSON ANTES de enviar (si falla, no se envía nada)
        String destinatariosJson;
        String adjuntosJson;
        try {
            destinatariosJson = objectMapper.writeValueAsString(destinatarios);
            adjuntosJson      = objectMapper.writeValueAsString(adjuntos);
        } catch (Exception e) {
            log.error("❌ No se pudo serializar destinatarios/adjuntos. Doc: {}", documentoId, e);
            throw new BusinessRuleException("Error interno al preparar el correo. Intente nuevamente.");
        }

        // 8. Enviar correo
        List<String> correos = destinatarios.stream()
                .map(d -> d.get("email"))
                .toList();

        String error = emailPort.enviarConAdjuntos(correos, asunto, cuerpo, adjuntos);

        if (error != null) {
            log.error("❌ Envío fallido. Doc: {} · Destinatarios: {} · Error: {}",
                    documentoId, correos, error);
            throw new BusinessRuleException("Error al enviar el correo: " + error);
        }

        // 9. Persistir registro del envío
        try {
            EnvioCorreo envio = new EnvioCorreo(
                    null,
                    expediente.getId(),
                    asunto,
                    cuerpo,
                    destinatariosJson,
                    adjuntosJson,
                    null,                       // fecha_envio = now() por defecto
                    null,
                    2,
                    false
            );
            envioCorreoRepositoryPort.guardar(envio);

        } catch (Exception e) {
            log.error("⚠️ CORREO ENVIADO pero no se pudo registrar en BD. Doc: {} · Error: {}",
                    documentoId, e.getMessage(), e);
        }

        // 10. Actualizar estados (documento + expediente)
        doc.marcarComoEnviado();
        documentoTesisRepositoryPort.guardar(doc);

        expediente.marcarComoMemoEnviado();
        expedienteRepositoryPort.guardar(expediente);

        log.info("Memo enviado y registrado. Destinatarios: {} · Adjuntos: {}",
                destinatarios.size(), adjuntos.size());
    }

    @Override
    public ContenidoEmailMemo obtenerContenidoMemoAEnviar(Long expedienteId) {
        log.info("Obteniendo la vista de envio email de un expediente. [ EXPEDIENTE : {} ]", expedienteId);

        ExpedienteTesis expediente = expedienteRepositoryPort.buscarPorId(expedienteId)
                .orElseThrow(() -> new ResourceNotFoundException("ExpedienteTesis", "id", expedienteId.toString()));

        DocumentoTesisDetalleView doc = documentoTesisRepositoryPort.obtenerMemoPorExpediente(expedienteId);
        List<ExpedienteArchivo> expedientes = expedienteArchivoRepositoryPort.obtenerPorExpediente(expedienteId);

        ComisionDetalleView comision = gestionarComisionUseCase
                .obtenerPorTesisYEtapa(expediente.getTesisId(), expediente.getEtapaCodigo());

        TesisDetalleView tesis = tesisRepositoryPort.obtenerDetallePorId(expediente.getTesisId());

        List<DestinatarioEmail> destinatarios = new ArrayList<>();

        if (comision.getMiembros() != null) {
            for (ComisionMiembroDetalleView m : comision.getMiembros()) {
                if (m.correoInstitucional() == null || m.correoInstitucional().isBlank()) continue;
                destinatarios.add(new  DestinatarioEmail(
                        m.correoInstitucional(),
                        m.docenteNombre(),
                        m.rolDenominacion()
                ));
            }
        }

        if (tesis.getAutores() != null) {
            for (AutorView autor : tesis.getAutores()) {
                String correo = autor.correoInstitucional();
                if (correo == null || correo.isBlank()) continue;

                String nombreCompleto = String.format("%s %s %s",
                        autor.nombres(),
                        autor.primerApellido(),
                        autor.segundoApellido() != null ? autor.segundoApellido() : ""
                ).trim();

                destinatarios.add(new DestinatarioEmail(
                        correo,
                        nombreCompleto,
                        "Autor"
                ));
            }
        }

        if (destinatarios.isEmpty()) {
            throw new BusinessRuleException("No hay destinatarios con correo registrado (ni comisión ni autores).");
        }

        List<ExpedienteArchivoDetalleView> adjuntos = new ArrayList<>();
        String nombreMemo = "MM N° " + String.format("%03d", doc.getNumeroCorrelativo()) + "-" + doc.getAnioCorrelativo() + "-UNSCH-FCSA-D.pdf";
        if (doc.getArchivoPdfUrl() != null) {
            adjuntos.add(new ExpedienteArchivoDetalleView(
                    doc.getId(),
                    nombreMemo,
                    doc.getArchivoPdfUrl(),
                    doc.getTamanioBytesPdf()
            ));
        }

        for (ExpedienteArchivo arch : expedientes) {
            if (arch.getArchivoUrl() != null && !arch.getArchivoUrl().isBlank()) {
                adjuntos.add(new  ExpedienteArchivoDetalleView(
                        arch.getId(),
                        arch.getNombreArchivo(),
                        arch.getArchivoUrl(),
                        arch.getTamanioBytes()
                ));
            }
        }

        String tipoMemo = mapearNombreTipoDocumento(expediente.getEtapaCodigo());
        String asunto = "Revision de " + tipoMemo + " de Tesis";
        String cuerpo = """
            Estimados miembros de la comisión y autores:

            Se adjunta el %s referido a la revisión y aprobación
            de la tesis del expediente N° %s.

            Además, se adjuntan los documentos enviados por el tesista en su expediente.

            Atentamente,
            Secretaría de Facultad - FCSA UNSCH
            """.formatted(
                nombreMemo,
                expediente.getNumeroExpediente()
        );

        return new ContenidoEmailMemo(
                doc.getId(),
                destinatarios,
                asunto,
                cuerpo,
                adjuntos
        );

    }

    private boolean esTipoMemo(String tipoCodigo) {
        return TablaMaestraCodigos.TIPO_DOCUMENTO_MEMO_PROYECTO.equals(tipoCodigo)
                || TablaMaestraCodigos.TIPO_DOCUMENTO_MEMO_BORRADOR.equals(tipoCodigo);
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
