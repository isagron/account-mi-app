package com.isagron.accountmi_server.web_filters;

import com.isagron.accountmi_server.config.properties.SecurityProperties;
import com.isagron.accountmi_server.exceptions.UserIdParamNotMatchToken;
import com.isagron.accountmi_server.security.FirebaseUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Slf4j
public class UserIdInjectWebFilter implements WebFilter {

    @Autowired
    SecurityProperties restSecProps;

    public UserIdInjectWebFilter(SecurityProperties restSecProps){
        this.restSecProps = restSecProps;
    }

    protected boolean shouldFilter(String path) {
        return restSecProps.getAllowedPublicApis()
                .stream().noneMatch(excludePath -> new AntPathMatcher().match(excludePath, path));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (shouldFilter(exchange.getRequest().getPath().value())) {

            return ReactiveSecurityContextHolder.getContext()
                    .map(SecurityContext::getAuthentication)
                    .map(Authentication::getPrincipal)
                    .cast(FirebaseUser.class)
                    .flatMap(user -> {
                        String userId = exchange.getRequest().getQueryParams().getFirst("userId");
                        if (userId != null && !userId.equals(user.getUid())) {
                            throw new UserIdParamNotMatchToken();
                        }
                        if (userId == null) {
                            UriComponents newUri = UriComponentsBuilder.fromUri(exchange.getRequest().getURI()).queryParam("userId", Arrays.asList(user.getUid())).build();
                            ServerHttpRequest newRequest = exchange.getRequest().mutate().uri((newUri.toUri())).build();
                            return chain.filter(exchange.mutate().request(newRequest).build());
                        }
                        return chain.filter(exchange);
                    });

        } else {
            return chain.filter(exchange);
        }

    }
}
