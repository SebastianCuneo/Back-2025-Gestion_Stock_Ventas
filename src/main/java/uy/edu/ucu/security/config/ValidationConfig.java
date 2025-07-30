package uy.edu.ucu.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uy.edu.ucu.security.services.models.validation.UserValidation;

@Configuration
public class ValidationConfig {
@Bean
    public UserValidation userValidation() {
    return new UserValidation();
}
}
