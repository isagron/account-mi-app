package com.isagron.accountmi_server.firebase.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FirebaseUser {

    private String email;

    private String localId;

    private String idToken;

    private String refreshToken;

    private String expiresIn;
}
