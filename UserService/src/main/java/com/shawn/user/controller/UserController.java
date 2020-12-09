package com.shawn.user.controller;

import com.shawn.user.exception.SignUpException;
import com.shawn.user.model.dto.SignUpReq;
import com.shawn.user.model.dto.CarTypeDto;
import com.shawn.user.model.dto.DriverDto;
import com.shawn.user.model.dto.SignUpSuccessResponse;
import com.shawn.user.model.dto.UserLocationDto;
import com.shawn.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
            case DRIVER -> {
                if (Optional.ofNullable(signUpReq.getCarType()).isEmpty()) {
                    throw new SignUpException("Apply to become a driver, car type cannot be empty when.");
                } else {
                    signUpSuccessResponseDto = Optional.of(userService.signUpAsDriver(signUpReq));
                }
            }
            case PASSENGER -> signUpSuccessResponseDto = Optional.of(userService.signUpAsPassenger(signUpReq));
        }
        return signUpSuccessResponseDto.get();
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

    @GetMapping("/carType/type/{carTypeName}")
    public CarTypeDto getCarTypeByName(@PathVariable String carTypeName) {
        return userService.getCarType(carTypeName);
    }
}
