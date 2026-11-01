package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.storage;

import com.deepoove.poi.XWPFTemplate;
import lombok.extern.slf4j.Slf4j;
import org.jodconverter.core.DocumentConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.port.out.storage.DocumentoGeneradorPort;
import unsch.fcsa.sigte.application.port.out.storage.StoragePort;
import unsch.fcsa.sigte.application.port.out.tramites.dto.DocumentoGenerado;
import unsch.fcsa.sigte.application.port.out.tramites.dto.MemorandumData;
import unsch.fcsa.sigte.domain.exception.BusinessRuleException;
import unsch.fcsa.sigte.domain.util.FechaEspanol;
import unsch.fcsa.sigte.domain.util.PathSanitizer;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DocumentoGeneradorAdapter implements DocumentoGeneradorPort {

    private final DocumentConverter documentConverter;
    private final Path storageRoot;
    private final StoragePort storagePort;

    public DocumentoGeneradorAdapter(
            DocumentConverter documentConverter,
            @Value("${app.storage.location}") String storageLocation,
            StoragePort storagePort) {
        this.documentConverter = documentConverter;
        this.storageRoot = Paths.get(storageLocation).toAbsolutePath().normalize();
        this.storagePort = storagePort;
    }

    @Override
    public DocumentoGenerado generarMemorandum(MemorandumData data) {
        log.info("Generando memorando N° {}-{}", data.numeroMemo(), data.anio());

        Path carpetaSalida = prepararCarpetaSalida();
        String baseNombre = PathSanitizer.sanitizeRelativePath("MM N° " + data.numeroMemo() + "-" + data.anio() + " " + data.tesistas());

        Path rutaWord = carpetaSalida.resolve(baseNombre + ".docx");
        Path rutaPdf  = carpetaSalida.resolve(baseNombre + ".pdf");

        renderizarWord(data, rutaWord);
        convertirAPdf(rutaWord, rutaPdf);

        try {
            long sizeWord = Files.size(rutaWord);
            long sizePdf  = Files.size(rutaPdf);

            String rutaWordRel = storageRoot.relativize(rutaWord).toString().replace("\\", "/");
            String rutaPdfRel  = storageRoot.relativize(rutaPdf).toString().replace("\\", "/");

            log.info("Memorando generado. Word: {} ({} B) · PDF: {} ({} B)",
                    rutaWordRel, sizeWord, rutaPdfRel, sizePdf);

            return new DocumentoGenerado(rutaWordRel, rutaPdfRel, sizeWord, sizePdf);

        } catch (Exception e) {
            throw new BusinessRuleException("Error al medir los archivos generados: " + e.getMessage());
        }
    }

    private Path prepararCarpetaSalida() {
        try {
            Path carpeta = storageRoot.resolve("documentos/memos");
            Files.createDirectories(carpeta);
            return carpeta;
        } catch (Exception e) {
            throw new BusinessRuleException("No se pudo preparar la carpeta de salida: " + e.getMessage());
        }
    }

    private void renderizarWord(MemorandumData data, Path rutaWord) {
        Resource plantillaRes = storagePort.cargarComoRecurso(data.rutaPlantilla());

        try (InputStream plantilla = plantillaRes.getInputStream();
             OutputStream salida  = Files.newOutputStream(rutaWord)) {

            XWPFTemplate template = XWPFTemplate.compile(plantilla).render(construirModelo(data));
            template.write(salida);
            template.close();

        } catch (Exception e) {
            throw new BusinessRuleException(
                    "No se pudo generar el Word. Plantilla: '" + data.rutaPlantilla()
                            + "'. Detalle: " + e.getMessage());
        }
    }

    private void convertirAPdf(Path rutaWord, Path rutaPdf) {
        try {
            documentConverter.convert(rutaWord.toFile())
                    .to(rutaPdf.toFile())
                    .execute();
        } catch (Exception e) {
            throw new BusinessRuleException(
                    "No se pudo convertir el Word a PDF. Detalle: " + e.getMessage());
        }
    }

    private Map<String, Object> construirModelo(MemorandumData data) {
        Map<String, Object> modelo = new HashMap<>();

        modelo.put("numeroMemo", data.numeroMemo());
        modelo.put("anio", data.anio());
        modelo.put("asunto", data.asunto());
        modelo.put("tesistas", data.tesistas());
        modelo.put("numeroExpediente", data.numeroExpediente());
        modelo.put("fechaEmision", FechaEspanol.formatear(data.fechaEmision()));
        modelo.put("tipoDocumento", data.tipoDocumento());
        modelo.put("tituloTesis", data.tituloTesis());

        List<Map<String, String>> miembros = data.miembros().stream()
                .map(m -> Map.of("nombre", m.nombre(), "rol", m.rol()))
                .toList();
        modelo.put("miembros", miembros);

        return modelo;
    }

}
