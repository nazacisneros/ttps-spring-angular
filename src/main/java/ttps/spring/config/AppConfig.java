package ttps.spring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration

@ComponentScan(basePackages = {
        "ttps.spring.service",
        "ttps.spring.controller",
        "ttps.spring.model",
})
public class AppConfig {

}