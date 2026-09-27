package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.security;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.port.out.security.CurrentUserPort;

@Component
public class CurrentUserAdapter implements CurrentUserPort {
    @Override
    public String getCurrentUser() {
        return "admin";
    }
}
