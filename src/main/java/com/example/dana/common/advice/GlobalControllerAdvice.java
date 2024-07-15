package com.example.dana.common.advice;

import com.example.dana.common.exception.UserHandleException;
import com.example.dana.common.response.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseWrapper handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseWrapper.FAILURE(HttpStatus.BAD_REQUEST.toString(), errors);
    }

    @ExceptionHandler(UserHandleException.class)
    private ResponseWrapper userHandleException(UserHandleException ex) {
        return ResponseWrapper.FAILURE(HttpStatus.BAD_REQUEST.toString(), ex.getMessage());
    }
}
