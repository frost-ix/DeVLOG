package io.devlog.blog.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://jungsonghun.iptime.org")
                .allowedOrigins("http://localhost:8443")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE");
    }
}