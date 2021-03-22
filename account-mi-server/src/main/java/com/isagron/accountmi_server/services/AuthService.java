package com.isagron.accountmi_server.services;

import com.isagron.accountmi_api.dtos.user_mng.AuthUserDto;
import com.isagron.accountmi_api.dtos.user_mng.LoginRequest;
import com.isagron.accountmi_api.dtos.user_mng.RefreshTokenResponse;
import com.isagron.accountmi_api.dtos.user_mng.SignupRequestDto;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<AuthUserDto> register(SignupRequestDto signupRequest);

    Mono<AuthUserDto> login(LoginRequest loginRequest);

    Mono<RefreshTokenResponse> refreshToken(String refreshToken);

    void logout(String token);
}
