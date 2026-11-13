package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.base;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponseDTO {

    private List<?> data;
    private Long totalItems;
    private Integer totalPages;
    private Integer currentPage;
    private Integer size;
    private boolean isFirst;
    private boolean isLast;
    private String message;
    private String status;
    private String code;

}
