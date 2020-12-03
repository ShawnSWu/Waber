package com.shawn.user.service;

import com.shawn.user.model.dto.SignUpFormDto;
import com.shawn.user.model.dto.response.DriverDto;
import com.shawn.user.model.dto.response.SignUpSuccessResponseDto;
import com.shawn.user.model.dto.response.UserLocationDto;

public interface UserService {

    SignUpSuccessResponseDto signUpAsPassenger(SignUpFormDto signUpFormDto);

    SignUpSuccessResponseDto signUpAsDriver(SignUpFormDto signUpFormDto);

    void updateLocation(long userId, double latitude, double longitude);

    UserLocationDto getUserLatestLocation(long userId);

    DriverDto getDriver(long driverId);
}
