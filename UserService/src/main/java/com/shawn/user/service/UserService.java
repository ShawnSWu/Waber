package com.shawn.user.service;

import com.shawn.user.model.dto.SignUpFormDto;
import com.shawn.user.model.dto.response.SignUpSuccessResponseDto;

public interface UserService {

    SignUpSuccessResponseDto signUpAsPassenger(SignUpFormDto signUpFormDto);

    SignUpSuccessResponseDto signUpAsDriver(SignUpFormDto signUpFormDto);

    void updateLocation(long userId, double latitude, double longitude);
}
