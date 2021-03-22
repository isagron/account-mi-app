package com.isagron.accountmi_server.firebase.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FbUserAuthenticationWithPassword {

    private String email;

    private String password;

    private boolean returnSecureToken;
}
