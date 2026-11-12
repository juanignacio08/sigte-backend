package unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.maestro;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.command.files.ArchivoSubida;
import unsch.fcsa.sigte.application.command.maestro.ActualizarPlantillaCommand;
import unsch.fcsa.sigte.application.command.maestro.RegistrarPlantillaCommand;
import unsch.fcsa.sigte.application.port.out.maestro.dtos.PlantillaDocumentoDetalleView;
import unsch.fcsa.sigte.domain.model.maestro.PlantillaDocumento;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.ActualizarPlantillaDocumentoRequest;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.PlantillaDocumentoResponse;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.RegistrarPlantillaDocumentoRequest;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.file.MultipartFileMapper;

@Component
public class PlantillaDocumentoRestMapper {

    private final MultipartFileMapper multipartFileMapper;

    public PlantillaDocumentoRestMapper(MultipartFileMapper multipartFileMapper) {
        this.multipartFileMapper = multipartFileMapper;
    }

    public RegistrarPlantillaCommand toCommand(RegistrarPlantillaDocumentoRequest request) {
        ArchivoSubida archivo = request.archivoDocx() != null
                ? multipartFileMapper.toArchivosSubida(request.archivoDocx())
                : null;

        return new RegistrarPlantillaCommand(
                request.escuelaId(),
                request.tipoDocumentoCodigo(),
                request.plazoDias(),
                request.plazoMeses(),
                archivo
        );
    }

    public ActualizarPlantillaCommand toCommand(Long id, ActualizarPlantillaDocumentoRequest request) {
        ArchivoSubida archivo = request.archivoDocx() != null
                ? multipartFileMapper.toArchivosSubida(request.archivoDocx())
                : null;

        return new ActualizarPlantillaCommand(
                id,
                request.plazoDias(),
                request.plazoMeses(),
                archivo
        );
    }

    public PlantillaDocumentoResponse toResponse(PlantillaDocumento domain) {
        if (domain == null) return null;

        return PlantillaDocumentoResponse.builder()
                .id(domain.getId())
                .escuelaId(domain.getEscuelaId())
                .nombreDocumento(domain.getNombreDocumento())
                .tipoDocumentoCodigo(domain.getTipoDocumentoCodigo())
                .archivoUrl(domain.getArchivoUrl())
                .plazoDias(domain.getPlazoDias())
                .plazoMeses(domain.getPlazoMeses())
                .estado(domain.getEstado())
                .build();
    }

    public PlantillaDocumentoResponse toResponse(PlantillaDocumentoDetalleView view) {
        if (view == null) return null;

        return PlantillaDocumentoResponse.builder()
                .id(view.id())
                .escuelaId(view.escuelaId())
                .nombreDocumento(view.nombreDocumento())
                .tipoDocumentoCodigo(view.tipoDocumentoCodigo())
                .tipoDocumentoDenominacion(view.tipoDocumentoDenominacion())
                .archivoUrl(view.archivoUrl())
                .plazoDias(view.plazoDias())
                .plazoMeses(view.plazoMeses())
                .estado(view.estado())
                .build();
    }
}