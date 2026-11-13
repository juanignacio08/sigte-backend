package unsch.fcsa.sigte.domain.exception;

public class DuplicateKeyException extends DomainException {
    public DuplicateKeyException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("Ya existe un registro de %s con %s: '%s'", resourceName, fieldName, fieldValue));
    }
}
