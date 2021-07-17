package com.isagron.accountmi_server.controllers.handlers;

import com.isagron.accountmi_api.dtos.errors.ErrorDto;
import com.isagron.accountmi_server.exceptions.FirebaseException;
import com.isagron.accountmi_server.exceptions.UserAlreadyExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class ValidationExceptionHandler {

    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorDto handleUserAlreadyExistException(UserAlreadyExistException e){
        return ErrorDto.builder()
                .message(e.getMessage())
                .errorCode(HttpStatus.BAD_REQUEST)
                .build();
    }

    @ExceptionHandler(FirebaseException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorDto handleUserAlreadyExistException(FirebaseException e){
        return ErrorDto.builder()
                .message(e.getError().getMessage())
                .errorCode(HttpStatus.BAD_REQUEST)
                .build();
    }
}
