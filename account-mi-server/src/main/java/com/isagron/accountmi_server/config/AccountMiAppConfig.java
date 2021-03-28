package com.isagron.accountmi_server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isagron.accountmi_server.config.properties.SecurityProperties;
import com.isagron.accountmi_server.web_filters.UserIdInjectWebFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountMiAppConfig {

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Bean
    @ConditionalOnProperty(
            value = "web-filters.inject-user-id",
            havingValue = "true"
    )
    public UserIdInjectWebFilter userIdInjectWebFilter(SecurityProperties securityProperties){
        return new UserIdInjectWebFilter(securityProperties);
    }
}
