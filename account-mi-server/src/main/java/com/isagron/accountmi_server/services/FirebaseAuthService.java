package com.isagron.accountmi_server.services;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.isagron.accountmi_api.dtos.user_mng.AuthUserDto;
import com.isagron.accountmi_api.dtos.user_mng.LoginRequest;
import com.isagron.accountmi_api.dtos.user_mng.RefreshTokenResponse;
import com.isagron.accountmi_api.dtos.user_mng.SignupRequestDto;
import com.isagron.accountmi_server.domain.dao.UserRepository;
import com.isagron.accountmi_server.exceptions.UserAlreadyExistException;
import com.isagron.accountmi_server.firebase.FirebaseClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Calendar;

@Service
@Log4j2
public class FirebaseAuthService implements AuthService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FirebaseClient firebaseClient;


    @Override
    public Mono<AuthUserDto> register(SignupRequestDto signupRequest) {
        return firebaseClient.singup(signupRequest.getEmail(), signupRequest.getPassword())
                .map(firebaseUser -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.SECOND, Integer.parseInt(firebaseUser.getExpiresIn()));
                    return AuthUserDto.builder()
                            .expiresIn(Long.parseLong(firebaseUser.getExpiresIn()))
                            .email(firebaseUser.getEmail())
                            .refreshToken(firebaseUser.getRefreshToken())
                            .token(firebaseUser.getIdToken())
                            .userId(firebaseUser.getLocalId())
                            .expirationTime(calendar.getTime())
                            .build();
                });
    }

    @Override
    public Mono<AuthUserDto> login(LoginRequest loginRequest) {
        return firebaseClient.signinWithEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword())
                .map(firebaseUser -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.SECOND, Integer.parseInt(firebaseUser.getExpiresIn()));
                    return AuthUserDto.builder()
                            .expiresIn(Long.parseLong(firebaseUser.getExpiresIn()))
                            .email(firebaseUser.getEmail())
                            .refreshToken(firebaseUser.getRefreshToken())
                            .token(firebaseUser.getIdToken())
                            .userId(firebaseUser.getLocalId())
                            .expirationTime(calendar.getTime())
                            .build();
                })
                .onErrorMap(e -> e);
    }

    @Override
    public Mono<RefreshTokenResponse> refreshToken(String refreshToken){
        return firebaseClient.refreshToken(refreshToken)
                .map(fbRefreshTokenResponse -> RefreshTokenResponse.builder()
                        .userId(fbRefreshTokenResponse.getUserId())
                        .token(fbRefreshTokenResponse.getIdToken())
                        .refreshToken(fbRefreshTokenResponse.getRefreshToken())
                        .expiresIn(Long.parseLong(fbRefreshTokenResponse.getExpiresIn()))
                        .build());
    }

    @Override
    public void logout(String token){
        firebaseClient.signout(token);
    }




}
