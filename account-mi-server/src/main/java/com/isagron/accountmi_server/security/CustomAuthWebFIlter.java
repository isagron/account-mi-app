package com.isagron.accountmi_server.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.server.ServerWebExchange;

public class CustomAuthWebFIlter extends AuthenticationWebFilter {

    public CustomAuthWebFIlter(ReactiveAuthenticationManager authenticationManager) {
        super(authenticationManager);

    }

    public CustomAuthWebFIlter(ReactiveAuthenticationManagerResolver<ServerWebExchange> authenticationManagerResolver) {
        super(authenticationManagerResolver);
    }
}
