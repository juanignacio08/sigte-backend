package unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.file;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import unsch.fcsa.sigte.application.command.files.ArchivoSubida;
import unsch.fcsa.sigte.domain.exception.BusinessRuleException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MultipartFileMapper {

    public ArchivoSubida toArchivosSubida(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            return new ArchivoSubida(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize(),
                    file.getBytes()
            );
        } catch (IOException e) {
            throw new BusinessRuleException("No se pudo leer el archivo: " + file.getOriginalFilename());
        }
    }

    public List<ArchivoSubida> toArchivosSubida(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new BusinessRuleException("Debe adjuntar al menos un archivo PDF al expediente.");
        }

        List<ArchivoSubida> resultado = new ArrayList<>(files.size());
        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) continue;
            try {
                resultado.add(new ArchivoSubida(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getSize(),
                        file.getBytes()
                ));
            } catch (IOException e) {
                throw new BusinessRuleException("No se pudo leer el archivo: " + file.getOriginalFilename());
            }
        }

        if (resultado.isEmpty()) {
            throw new BusinessRuleException("Debe adjuntar al menos un archivo PDF válido.");
        }
        return resultado;
    }
}
