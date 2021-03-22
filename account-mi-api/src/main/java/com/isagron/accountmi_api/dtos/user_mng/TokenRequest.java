package com.isagron.accountmi_api.dtos.user_mng;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequest {

    @NotBlank
    private String token;
}
