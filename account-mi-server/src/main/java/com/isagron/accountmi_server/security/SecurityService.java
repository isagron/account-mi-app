package com.isagron.accountmi_server.security;

import com.isagron.accountmi_server.config.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.netty.http.server.HttpServerRequest;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class SecurityService {

    @Autowired
    SecurityProperties securityProps;

    public String getBearerToken(ServerHttpRequest request) {
        String bearerToken = null;

        String auth = Optional.ofNullable(request.getHeaders().get("authorization"))
                .map(headers -> headers.get(0))
                .orElse(null);
        if (auth == null) {
            auth = Optional.ofNullable(request.getQueryParams().get("authorization"))
                    .map(authorization -> authorization.get(0))
                    .orElse(null);
        }
//        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
//            bearerToken = authorization.substring(7);
//        }
        return auth;
    }

    public FirebaseUser getUser() {
        FirebaseUser userPrincipal = null;
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Object principal = securityContext.getAuthentication().getPrincipal();
        if (principal instanceof FirebaseUser) {
            userPrincipal = ((FirebaseUser) principal);
        }
        return userPrincipal;
    }

    public Credentials getCredentials() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return (Credentials) securityContext.getAuthentication().getCredentials();
    }

    public boolean isPublic(ServerHttpRequest serverHttpRequest) {
        return securityProps.getAllowedPublicApis().contains(serverHttpRequest.getPath().value());
    }
}
