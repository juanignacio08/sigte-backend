package unsch.fcsa.sigte.domain.exception;

public class ResourceNotFoundException extends DomainException {
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("No se encontró el recurso [%s] con %s: '%s'", resourceName, fieldName, fieldValue));
    }
}
