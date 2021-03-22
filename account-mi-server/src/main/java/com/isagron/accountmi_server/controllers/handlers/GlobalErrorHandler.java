package com.isagron.accountmi_server.controllers.handlers;

import com.isagron.accountmi_api.dtos.errors.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalErrorHandler {

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorDto handleGlobalError(Exception ex) {
        log.error("Global error handler exception: ", ex);
        return ErrorDto.builder()
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(ex.getMessage())
                .build();
    }

}
