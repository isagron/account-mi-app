package com.isagron.accountmi_server.firebase;

import com.isagron.accountmi_server.exceptions.FirebaseException;
import com.isagron.accountmi_server.exceptions.UserAlreadyExistException;
import com.isagron.accountmi_server.firebase.dtos.FirebaseErrorResponse;
import org.springframework.stereotype.Component;

@Component
public class FirebaseExceptionConverter {

    private static final String EMAIL_EXISTS = "EMAIL_EXISTS";
    public RuntimeException convert(FirebaseErrorResponse firebaseError){
        return switch (firebaseError.getError().getMessage()){
            case EMAIL_EXISTS -> new UserAlreadyExistException();
            default -> new FirebaseException(firebaseError.getError());
        };
    }
}
