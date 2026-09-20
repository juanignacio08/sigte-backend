package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.security;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.domain.port.out.security.CurrentUserPort;

@Component
public class CurrentUserAdapter implements CurrentUserPort {
    @Override
    public Long getCurrentUserId() {
        return 1L;
    }
}
