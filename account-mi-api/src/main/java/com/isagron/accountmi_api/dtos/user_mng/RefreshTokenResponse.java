package com.isagron.accountmi_api.dtos.user_mng;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenResponse {

    private String userId;

    private String token;

    private String refreshToken;

    private long expiresIn;
}

