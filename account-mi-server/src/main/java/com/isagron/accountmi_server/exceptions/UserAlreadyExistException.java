package com.isagron.accountmi_server.exceptions;

public class UserAlreadyExistException extends RuntimeException{

    public UserAlreadyExistException(){
        super("User with the same email already exist");
    }
}
