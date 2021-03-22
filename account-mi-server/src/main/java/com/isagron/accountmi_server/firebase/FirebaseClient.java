package com.isagron.accountmi_server.firebase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.isagron.accountmi_server.config.properties.FirebaseProperties;
import com.isagron.accountmi_server.config.properties.SecurityProperties;
import com.isagron.accountmi_server.exceptions.FirebaseException;
import com.isagron.accountmi_server.firebase.dtos.FbRefreshTokenRequest;
import com.isagron.accountmi_server.firebase.dtos.FbRefreshTokenResponse;
import com.isagron.accountmi_server.firebase.dtos.FbUserAuthenticationWithPassword;
import com.isagron.accountmi_server.firebase.dtos.FirebaseErrorResponse;
import com.isagron.accountmi_server.firebase.dtos.FirebaseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

@Service
public class FirebaseClient {

    private String firebaseHost = "identitytoolkit.googleapis.com";
    private String firebaseAccountsUrl = "/v1/accounts";
    private String firebaseTokensUrl = "/v1/token";
    private String firebaseSignUpUrl = "signUp";
    private String firebaseSigninWithPassword = "signInWithPassword";

    @Autowired
    private WebClient webClient;

    @Autowired
    private FirebaseProperties firebaseProperties;


    @Autowired
    private FirebaseExceptionConverter exceptionConverter;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void onApplicationEvent() throws IOException {
        if (this.firebaseProperties.isEnable()) {
            InputStream serviceAccount = this.firebaseProperties.getAccount().getAsInputStream();
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
        }
    }



    public Mono<FirebaseUser> singup(String email, String password){
        URI uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(firebaseHost)
                .path(firebaseAccountsUrl)
                .path(":")
                .path(firebaseSignUpUrl)
                .queryParam("key", this.firebaseProperties.getApikey())
                .build().toUri();

        FbUserAuthenticationWithPassword data = FbUserAuthenticationWithPassword.builder()
                .email(email)
                .password(password)
                .returnSecureToken(true)
                .build();

        return webClient.post()
                .uri(uri)
                .body(BodyInserters.fromValue(data))
                .retrieve()
                .onStatus(HttpStatus::isError, response -> {
                    return response.bodyToMono(FirebaseErrorResponse.class).flatMap(error -> {
                        return Mono.error(exceptionConverter.convert(error));
                    });
                })
                .bodyToMono(FirebaseUser.class);
    }

    public Mono<FirebaseUser> signinWithEmailAndPassword(String email, String password){
        URI uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(firebaseHost)
                .path(firebaseAccountsUrl)
                .path(":")
                .path(firebaseSigninWithPassword)
                .queryParam("key", this.firebaseProperties.getApikey())
                .build().toUri();

        FbUserAuthenticationWithPassword data = FbUserAuthenticationWithPassword.builder()
                .email(email)
                .password(password)
                .returnSecureToken(true)
                .build();
        return webClient.post()
                .uri(uri)
                .body(BodyInserters.fromValue(data))
                .exchange()
                .flatMap(clientResponse -> {
                    return clientResponse.bodyToMono(FirebaseUser.class);
                });
    }

    public Mono<FbRefreshTokenResponse> refreshToken(String refreshToken){
        URI uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(firebaseHost)
                .path(firebaseTokensUrl)
                .queryParam("key", this.firebaseProperties.getApikey())
                .build().toUri();

        FbRefreshTokenRequest body = FbRefreshTokenRequest.builder()
                .grantType("refresh_token")
                .refreshToken(refreshToken)
                .build();
        return webClient.post().uri(uri).body(BodyInserters.fromValue(body)).retrieve().bodyToMono(FbRefreshTokenResponse.class);

    }

    public void signout(String token){
        try {
            FirebaseAuth.getInstance().revokeRefreshTokens(token);
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }
    }


}
