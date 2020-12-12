package com.shawn.user.controller;

import com.shawn.user.exception.SignUpException;
import com.shawn.user.model.dto.*;
import com.shawn.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public SignUpSuccessResponse signUp(@RequestBody SignUpReq signUpReq, @RequestParam String type) {
        final String DRIVER = "driver";
        final String PASSENGER = "passenger";
        Optional<SignUpSuccessResponse> signUpSuccessResponseDto = Optional.empty();
        switch (type) {
            case DRIVER: {
                if (Optional.ofNullable(signUpReq.getCarType()).isEmpty()) {
                    throw new SignUpException("Apply to become a driver, car type cannot be empty when.");
                }
                signUpSuccessResponseDto = Optional.of(userService.signUpAsDriver(signUpReq));
            }
            break;
            case PASSENGER:
                signUpSuccessResponseDto = Optional.of(userService.signUpAsPassenger(signUpReq));
                break;
        }
        return signUpSuccessResponseDto.orElseGet(() -> {
            throw new SignUpException("Sign Up failed.");
        });
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

}
