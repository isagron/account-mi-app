package com.isagron.accountmi_server.config;

import com.isagron.accountmi_server.config.properties.SecurityProperties;
import com.isagron.accountmi_server.security.CustomAuthenticationManger;
import com.isagron.accountmi_server.security.CustomSecurityContextRepository;
import com.isagron.accountmi_server.security.SecurityFilter;
import com.isagron.accountmi_server.security.SecurityService;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Profile({"!dev", "!unsecure"})
public class SecurityConfig {


    @Autowired
    SecurityProperties restSecProps;

    @Autowired
    CustomAuthenticationManger authenticationManger;

    @Autowired
    CustomSecurityContextRepository securityContextRepository;

    @Autowired
    SecurityService securityService;

    private SecurityFilter securityFilter(){
        return new SecurityFilter(restSecProps, securityService);
    }

    public ServerAuthenticationEntryPoint restAuthenticationEntryPoint() {
        return (exchange, e) -> {
            Map<String, Object> errorObject = new HashMap<>();
            int errorCode = 401;
            errorObject.put("message", "Unauthorized access of protected resource, invalid credentials");
            errorObject.put("error", HttpStatus.UNAUTHORIZED);
            errorObject.put("code", errorCode);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().setRawStatusCode(errorCode);
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory()
                    .allocateBuffer()
                    .write(mapObejct(errorObject).getBytes())));
        };
    }

    private String mapObejct(Object errorCode) {
        try {
            return new ObjectMapper().writeValueAsString(errorCode);
        } catch (IOException e) {
            e.printStackTrace();
            return "failed to get object";
        }
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(restSecProps.getAllowedOrigins());
        configuration.setAllowedMethods(restSecProps.getAllowedMethods());
        configuration.setAllowedHeaders(restSecProps.getAllowedHeaders());
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange()
                .pathMatchers(restSecProps.getAllowedPublicApis().toArray(String[]::new))
                .permitAll()
                .pathMatchers(HttpMethod.OPTIONS, "/**")
                .permitAll()
                .anyExchange()
                .authenticated()
                .and()
                .csrf().disable()
                .cors()
                .configurationSource(corsConfigurationSource())
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .addFilterBefore(securityFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint())
                .and()
                .build();
    }


}
