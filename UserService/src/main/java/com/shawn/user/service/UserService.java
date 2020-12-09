package com.shawn.user.service;

import com.shawn.user.model.dto.SignUpReq;
import com.shawn.user.model.dto.CarTypeDto;
import com.shawn.user.model.dto.DriverDto;
import com.shawn.user.model.dto.SignUpSuccessResponse;
import com.shawn.user.model.dto.UserLocationDto;

public interface UserService {

    SignUpSuccessResponse signUpAsPassenger(SignUpReq signUpReq);

    SignUpSuccessResponse signUpAsDriver(SignUpReq signUpReq);

    void updateLocation(long userId, double latitude, double longitude);

    UserLocationDto getUserLatestLocation(long userId);

    DriverDto getDriver(long driverId);

    CarTypeDto getCarType(long carType);

    CarTypeDto getCarType(String carTypeName);
}
