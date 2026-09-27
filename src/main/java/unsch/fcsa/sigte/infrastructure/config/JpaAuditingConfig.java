package unsch.fcsa.sigte.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import unsch.fcsa.sigte.application.port.out.security.CurrentUserPort;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<String> auditorProvider(CurrentUserPort currentUserPort) {
        return () -> Optional.ofNullable(currentUserPort.getCurrentUser());
    }
}
