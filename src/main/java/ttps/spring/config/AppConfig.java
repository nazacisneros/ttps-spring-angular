package ttps.spring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
// escanea sólo capas de negocio y web — evita el package repository
@ComponentScan(basePackages = {
        "ttps.spring.service",
        "ttps.spring.controller",
        "ttps.spring.model",
})
public class AppConfig {
    // beans de aplicación (si necesitás alguno lo declarás aquí)
}