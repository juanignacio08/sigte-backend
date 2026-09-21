package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.base;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponseDTO {

    private Long totalItems;
    private Integer totalPages;
    private Integer currentPage;
    private List<?> data;
    private String message;
    private String status;
    private String code;

}
