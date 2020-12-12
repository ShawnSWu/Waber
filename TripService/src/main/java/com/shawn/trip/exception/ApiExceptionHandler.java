package com.shawn.trip.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = NotFoundTripException.class)
    @ResponseBody
    public ResponseEntity<?> signUpFailed(NotFoundTripException e) {
        return new ResponseEntity<>(new NotFoundTripException(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
