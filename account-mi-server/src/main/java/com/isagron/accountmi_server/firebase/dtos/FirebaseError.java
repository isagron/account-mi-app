package com.isagron.accountmi_server.firebase.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirebaseError {

    private int code;
    private String message;
    private List<Object> errors;
}
