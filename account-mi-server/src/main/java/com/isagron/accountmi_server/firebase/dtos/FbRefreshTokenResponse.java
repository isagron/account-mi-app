package com.isagron.accountmi_server.firebase.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FbRefreshTokenResponse {

    @JsonProperty("expires_in")
    private String expiresIn;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("id_token")
    private String idToken;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("project_id")
    private String projectId;
}
