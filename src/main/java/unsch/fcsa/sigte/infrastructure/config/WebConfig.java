package unsch.fcsa.sigte.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica a todos los endpoints del backend
                .allowedOrigins("http://localhost:4200") // URLs de tu Frontend (Angular, React, Vue, etc.)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // Métodos HTTP permitidos
                .allowedHeaders("*") // Permite cualquier cabecera (Authorization, Content-Type, etc.)
                .allowCredentials(true) // Permite el envío de cookies o cabeceras de autenticación
                .maxAge(3600); // Tiempo en segundos que el navegador puede cachear la respuesta CORS preflight
    }
}
