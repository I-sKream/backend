package com.v1.iskream.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> handlerException(IllegalArgumentException e){
        String msg = e.getMessage();
        return new ResponseEntity<>(
                msg, HttpStatus.BAD_REQUEST
        );
    }
}
