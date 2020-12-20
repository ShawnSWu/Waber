package com.shawn.user.controller;

import com.shawn.user.model.dto.*;
import com.shawn.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/drivers")
    public SignUpSuccessResponse signUpAsDriver(@RequestBody DriverSignUpReq driverSignUpReq) {
        return userService.signUpAsDriver(driverSignUpReq);
    }

    @PostMapping("/passengers")
    public SignUpSuccessResponse signUpAsPassenger(@RequestBody PassengerSignUpReq passengerSignUpReq) {
        return userService.signUpAsPassenger(passengerSignUpReq);
    }


    @PostMapping("/users/signIn")
    public SignInSuccessResponse login(@RequestBody SignInReq signInReq) {
        return userService.signIn(signInReq);
    }

    @PutMapping("/users/{userId}/location")
    public void updateLocation(@PathVariable long userId, @RequestParam double latitude, @RequestParam double longitude) {
        userService.updateLocation(userId, latitude, longitude);
    }

    @GetMapping("/users/{driverId}/location")
    public UserLocationDto getUserLatestLocation(@PathVariable long driverId) {
        return userService.getUserLatestLocation(driverId);
    }

    @GetMapping("/users/{driverId}")
    public DriverDto getDriver(@PathVariable long driverId) {
        return userService.getDriver(driverId);
    }

    @GetMapping("/carType/id/{carTypeId}")
    public CarTypeDto getCarTypeById(@PathVariable long carTypeId) {
        return userService.getCarType(carTypeId);
    }

    @GetMapping("/carType/name/{carTypeName}")
    public CarTypeDto getCarTypeByName(@PathVariable String carTypeName) {
        return userService.getCarType(carTypeName);
    }

    @GetMapping("/activities/name/{activityName}")
    public ActivityResponse getActivityByName(@PathVariable String activityName) {
        return userService.getActivityByName(activityName);
    }

    @PostMapping("/activities/{activityName}/drivers/{driverId}")
    public void driverParticipateActivity(@PathVariable String activityName, @PathVariable long driverId) {
        userService.participateActivity(activityName, driverId);
    }

    @GetMapping("/activities/id/{activityId}")
    public ActivityResponse getActivity(@PathVariable long activityId) {
        return userService.getActivityById(activityId);
    }

    @GetMapping("/activities/{activityId}/carType/{carTypeId}")
    public List<DriverDto> getActivityDriver(@PathVariable long activityId, @PathVariable long carTypeId) {
        return userService.getDriverByActivityAndCarType(activityId, carTypeId);
    }

    @GetMapping("/activities/{activityName}/drivers")
    public List<ActivityDriverDto> getParticipatingDrivers(@PathVariable String activityName) {
        return userService.getParticipatingDrivers(activityName);
    }

}
