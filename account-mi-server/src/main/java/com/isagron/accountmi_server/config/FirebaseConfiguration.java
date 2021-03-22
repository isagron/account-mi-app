package com.isagron.accountmi_server.config;

import com.isagron.accountmi_server.config.properties.FirebaseProperties;
import com.isagron.accountmi_server.config.properties.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class FirebaseConfiguration {

    @Bean
    public FirebaseProperties firebaseProperties(SecurityProperties securityProperties){
        return securityProperties.getFirebaseProps();
    }
    @Bean
    public WebClient webClient(){
        return WebClient.builder().build();
    }
}
