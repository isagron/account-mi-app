package com.isagron.accountmi_server.firebase.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirebaseErrorResponse {

    private FirebaseError error;
}
