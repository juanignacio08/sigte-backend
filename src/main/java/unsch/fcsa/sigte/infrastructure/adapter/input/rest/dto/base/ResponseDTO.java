package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.base;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> {
    private T data;
    private String message;
    private String status;
    private String code;
}

