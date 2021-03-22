package com.isagron.accountmi_server.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.isagron.accountmi_server.config.properties.SecurityProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

//@Component
@Slf4j
@AllArgsConstructor
public class SecurityFilter implements WebFilter {

    //@Autowired
    private SecurityProperties securityProps;

    //@Autowired
    private SecurityService securityService;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        System.out.println(exchange.getRequest().getPath().value());
        //return chain.filter(exchange);
        Authentication a = verifyToken(exchange);
        if (a != null) {
            return chain.filter(exchange)
                    .subscriberContext(c -> ReactiveSecurityContextHolder.withAuthentication(a));
        } else {
            return chain.filter(exchange.mutate().build());
        }
    }

    public UsernamePasswordAuthenticationToken verifyToken(ServerWebExchange exchange) {
        String session = null;
        FirebaseToken decodeToken = null;
        Credentials.CredentialType type = null;
        String token = securityService.getBearerToken(exchange.getRequest());
        try {
            if (token != null && !token.equalsIgnoreCase("undefined")) {
                decodeToken = FirebaseAuth.getInstance().verifyIdToken(token);
                type = Credentials.CredentialType.ID_TOKEN;
            }
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            log.error("Firebase exception:: ", e.getLocalizedMessage());
        }
        FirebaseUser user = firebaseTokenToUserDto(decodeToken);
        //SecurityContextHolder.getContext().setAuthentication();
        UsernamePasswordAuthenticationToken authentication = null;
        if (user != null) {
            authentication = new UsernamePasswordAuthenticationToken(user,
                    new Credentials(type, decodeToken, token, session), null);
            //ReactiveSecurityContextHolder.withAuthentication(authentication);
            //SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        return authentication;

    }

    private FirebaseUser firebaseTokenToUserDto(FirebaseToken decodedToken) {
        FirebaseUser user = null;
        if (decodedToken != null) {
            user = new FirebaseUser();
            user.setUid(decodedToken.getUid());
            user.setName(decodedToken.getName());
            user.setEmail(decodedToken.getEmail());
            user.setPicture(decodedToken.getPicture());
            user.setIssuer(decodedToken.getIssuer());
            user.setEmailVerified(decodedToken.isEmailVerified());
        }
        return user;
    }
}
