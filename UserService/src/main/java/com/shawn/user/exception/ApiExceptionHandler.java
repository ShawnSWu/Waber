package com.shawn.user.exception;

import com.shawn.user.model.dto.response.error.ParticipateActivityFailedResponseDto;
import com.shawn.user.model.dto.response.error.SignUpFailedResponseDto;
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
        return new ResponseEntity<>(new SignUpFailedResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ParticipateActivityException.class)
    @ResponseBody
    public ResponseEntity<?> participateActivityFailed(ParticipateActivityException e) {
        return new ResponseEntity<>(new ParticipateActivityFailedResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }


}
