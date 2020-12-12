package com.shawn.user.service;

import com.shawn.user.model.dto.*;

import java.util.List;

public interface UserService {

    SignUpSuccessResponse signUpAsPassenger(SignUpReq signUpReq);

    SignUpSuccessResponse signUpAsDriver(SignUpReq signUpReq);

    void updateLocation(long userId, double latitude, double longitude);

    UserLocationDto getUserLatestLocation(long userId);

    DriverDto getDriver(long driverId);

    CarTypeDto getCarType(long carType);

    CarTypeDto getCarType(String carTypeName);

    void participateActivity(String activityName, long driverId);

    ActivityResponse getActivityById(long activityId);

    ActivityResponse getActivityByName(String activityName);

    List<DriverDto> getDriverByActivityAndCarType(long activityId, long carTypeId);
}
