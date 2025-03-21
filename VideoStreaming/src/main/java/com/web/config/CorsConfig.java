package com.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/video/stream/**")
                        .allowedOrigins("http://localhost:4200") 
                        .allowedMethods("GET","POST")
                        .allowCredentials(true)
                        .allowedHeaders("*")
                        .exposedHeaders("Content-Range", "Accept-Ranges", "Content-Length","Cache-Control");
            }
        };
    }
}
