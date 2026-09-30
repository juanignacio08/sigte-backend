package unsch.fcsa.sigte.infrastructure.adapter.input.rest.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import unsch.fcsa.sigte.domain.exception.BusinessRuleException;
import unsch.fcsa.sigte.domain.exception.DuplicateKeyException;
import unsch.fcsa.sigte.domain.exception.ResourceNotFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse response = ErrorResponse.build(
                "NOT_FOUND",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateKey(DuplicateKeyException ex) {
        ErrorResponse response = ErrorResponse.build(
                "DUPLICATE_KEY",
                ex.getMessage(),
                HttpStatus.CONFLICT.value(),
                null
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRule(BusinessRuleException ex) {
        ErrorResponse response = ErrorResponse.build(
                "BUSINESS_RULE_VIOLATION",
                ex.getMessage(),
                HttpStatus.UNPROCESSABLE_CONTENT.value(),
                null
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorResponse response = ErrorResponse.build(
                "BAD_REQUEST",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> erroresCampos = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String campo = (error instanceof FieldError fieldError) ? fieldError.getField() : error.getObjectName();
            erroresCampos.put(campo, error.getDefaultMessage());
        });

        ErrorResponse response = ErrorResponse.build(
                "VALIDATION_FAILED",
                "Uno o más campos de la petición no cumplen con las validaciones requeridas",
                HttpStatus.BAD_REQUEST.value(),
                erroresCampos
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Error no controlado en la aplicación: ", ex);

        ErrorResponse response = ErrorResponse.build(
                "INTERNAL_SERVER_ERROR",
                "Ha ocurrido un error interno no esperado. Contacte al administrador.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex) {
        ErrorResponse response = ErrorResponse.build(
                "BAD_REQUEST",
                "El archivo excede el tamaño máximo permitido (20 MB)",
                HttpStatus.BAD_REQUEST.value(),
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}