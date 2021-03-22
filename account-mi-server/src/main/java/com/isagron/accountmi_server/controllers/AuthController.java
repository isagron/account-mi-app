package com.isagron.accountmi_server.controllers;

import static com.isagron.accountmi_api.apis.v1.external.UserApi.Path.AUTH_PATH;
import static com.isagron.accountmi_api.apis.v1.external.UserApi.Path.REFRESH_TOKEN_PATH;
import static com.isagron.accountmi_api.apis.v1.external.UserApi.Path.REGISTER_PATH;
import static com.isagron.accountmi_api.apis.v1.external.UserApi.Path.SIGNIN_PATH;
import static com.isagron.accountmi_api.apis.v1.external.UserApi.Path.SIGNOUT_PATH;
import com.isagron.accountmi_api.dtos.user_mng.AuthUserDto;
import com.isagron.accountmi_api.dtos.user_mng.LoginRequest;
import com.isagron.accountmi_api.dtos.user_mng.RefreshTokenResponse;
import com.isagron.accountmi_api.dtos.user_mng.TokenRequest;
import com.isagron.accountmi_api.dtos.user_mng.SignupRequestDto;
import com.isagron.accountmi_server.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping(AUTH_PATH)
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(REGISTER_PATH)
    public Mono<AuthUserDto> signup(@RequestBody @Valid SignupRequestDto signupRequest) {
        return authService.register(signupRequest);
    }

    @PostMapping(SIGNIN_PATH)
    public Mono<AuthUserDto> signin(@RequestBody @Valid LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping(REFRESH_TOKEN_PATH)
    public Mono<RefreshTokenResponse> refreshToken(@RequestBody @Valid TokenRequest tokenRequest) {
        return authService.refreshToken(tokenRequest.getToken());
    }

    @PostMapping(SIGNOUT_PATH)
    public void signout(@RequestBody @Valid TokenRequest tokenRequest){
        authService.logout(tokenRequest.getToken());
    }


}
