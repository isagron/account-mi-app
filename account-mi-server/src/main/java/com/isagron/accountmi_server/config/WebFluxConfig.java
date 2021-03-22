package com.isagron.accountmi_server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isagron.accountmi_server.config.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@Profile({"integration", "dev", "unsecure"})
public class WebFluxConfig implements WebFluxConfigurer {

    @Autowired
    SecurityProperties restSecProps;

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .allowedOrigins(restSecProps.getAllowedOrigins().toArray(String[]::new))
                .allowedMethods(restSecProps.getAllowedMethods().toArray(String[]::new))
                .allowedHeaders(restSecProps.getAllowedHeaders().toArray(String[]::new))
                .maxAge(3600);
    }


}
