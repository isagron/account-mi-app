package com.isagron.accountmi_server.security;

import lombok.Data;

import java.io.Serializable;

@Data
public class FirebaseUser implements Serializable {
    private static final long serialVersionUID = 4408418647685225829L;

    private String uid;
    private String name;
    private String email;
    private boolean isEmailVerified;
    private String issuer;
    private String picture;
}
