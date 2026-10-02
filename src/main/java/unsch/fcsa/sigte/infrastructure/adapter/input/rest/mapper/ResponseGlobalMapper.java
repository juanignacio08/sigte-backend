package unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper;

import unsch.fcsa.sigte.application.command.PageResult;
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

    public static PaginatedResponseDTO buildPaginated(PageResult<?> pageable, String message) {
        return PaginatedResponseDTO.builder()
                .data(pageable.content())
                .totalItems(pageable.totalElements())
                .totalPages(pageable.totalPages())
                .currentPage(pageable.page())
                .size(pageable.size())
                .isFirst(pageable.isFirst())
                .isLast(pageable.isLast())
                .message(message)
                .status(SUCCESS)
                .code("200")
                .build();
    }

    public static PaginatedResponseDTO buildPaginated(PageResult<?> pageable, List<?> similarSource, String message) {
        return PaginatedResponseDTO.builder()
                .data(similarSource)
                .totalItems(pageable.totalElements())
                .totalPages(pageable.totalPages())
                .currentPage(pageable.page())
                .size(pageable.size())
                .isFirst(pageable.isFirst())
                .isLast(pageable.isLast())
                .message(message)
                .status(SUCCESS)
                .code("200")
                .build();
    }

}
