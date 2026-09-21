package unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper;

import org.springframework.data.domain.Page;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.base.PaginatedResponseDTO;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.base.ResponseDTO;

import java.util.List;

public class ResponseGlobalMapper {
    private static final String SUCCESS = "success";

    public static ResponseDTO<Object> build(Object result, String message, String code) {
        return ResponseDTO.builder()
                .data(result)
                .status(SUCCESS)
                .code(code)
                .message(message)
                .build();
    }

    public static PaginatedResponseDTO buildPaginated(Page<?> pageable, String message) {
        return PaginatedResponseDTO.builder()
                .data(pageable.getContent())
                .status(SUCCESS)
                .code("200")
                .message(message)
                .currentPage(pageable.getNumber())
                .totalItems(pageable.getTotalElements())
                .totalPages(pageable.getTotalPages())
                .build();
    }

    public static PaginatedResponseDTO buildPaginated(Page<?> pageable, List<?> similarSource) {
        return PaginatedResponseDTO.builder()
                .data(similarSource)
                .status(SUCCESS)
                .code("200")
                .currentPage(pageable.getNumber())
                .totalItems(pageable.getTotalElements())
                .totalPages(pageable.getTotalPages())
                .build();
    }

}
