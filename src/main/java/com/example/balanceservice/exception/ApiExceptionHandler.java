package com.example.balanceservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<Object> handleApiRequestException(RuntimeException e) {
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        final ApiException apiException = new ApiException(e.getMessage(), badRequest, LocalDateTime.now());
        return new ResponseEntity<>(apiException, badRequest);
    }
}
