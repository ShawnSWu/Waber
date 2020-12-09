package com.shawn.user.exception;

import com.shawn.user.model.dto.SignUpFailedResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = SignUpException.class)
    @ResponseBody
    public ResponseEntity<?> signUpFailed(SignUpException e) {
        return new ResponseEntity<>(new SignUpFailedResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
