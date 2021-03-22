package com.isagron.accountmi_api.dtos.user_mng;

import com.isagron.accountmi_api.validations.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @ValidEmail
    private String email;

    @NotBlank
    private String password;

}
