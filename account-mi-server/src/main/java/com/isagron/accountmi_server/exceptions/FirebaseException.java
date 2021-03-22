package com.isagron.accountmi_server.exceptions;

import com.isagron.accountmi_server.firebase.dtos.FirebaseError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FirebaseException extends RuntimeException {

    private FirebaseError error;

}
