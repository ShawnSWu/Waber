package com.shawn.match.exception;

import com.shawn.match.model.dto.response.ParticipateActivityFailedResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = ParticipateActivityException.class)
    @ResponseBody
    public ResponseEntity<?> participateActivityFailed(ParticipateActivityException e) {
        return new ResponseEntity<>(new ParticipateActivityFailedResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }


}
