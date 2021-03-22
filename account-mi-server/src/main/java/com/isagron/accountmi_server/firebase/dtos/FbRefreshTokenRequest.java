package com.isagron.accountmi_server.firebase.dtos;

import lombok.Builder;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;

@Builder
@Data
public class FbRefreshTokenRequest {

    @JsonProperty("grant_type")
    private String grantType;

    @JsonProperty("refresh_token")
    private String refreshToken;

}
