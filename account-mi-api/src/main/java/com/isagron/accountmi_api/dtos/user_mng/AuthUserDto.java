package com.isagron.accountmi_api.dtos.user_mng;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUserDto {

    private String userId;

    private String email;

    private String token;

    private String refreshToken;

    private long expiresIn;

    private Date expirationTime;
}
