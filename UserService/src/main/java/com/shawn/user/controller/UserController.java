package com.shawn.user.controller;

import com.shawn.user.exception.SignUpException;
import com.shawn.user.model.dto.SignUpFormDto;
import com.shawn.user.model.dto.response.SignUpSuccessResponseDto;
import com.shawn.user.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Data
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/users")
    public SignUpSuccessResponseDto signUp(@RequestBody SignUpFormDto signUpFormDto, @RequestParam String type) {
        final String DRIVER = "driver";
        final String PASSENGER = "passenger";
        Optional<SignUpSuccessResponseDto> signUpSuccessResponseDto = Optional.empty();
        switch (type) {
            case DRIVER -> {
                if (Optional.ofNullable(signUpFormDto.getCarType()).isEmpty()) {
                    throw new SignUpException("Apply to become a driver, car type cannot be empty when.");
                } else {
                    signUpSuccessResponseDto = Optional.of(userService.signUpAsDriver(signUpFormDto));
                }
            }
            case PASSENGER -> signUpSuccessResponseDto = Optional.of(userService.signUpAsPassenger(signUpFormDto));
        }
        return signUpSuccessResponseDto.get();
    }

    @PutMapping("/users/{userId}/location")
    public void driverParticipateActivity(@PathVariable long userId, @RequestParam double latitude, @RequestParam double longitude) {
        userService.updateLocation(userId, latitude, longitude);
    }

}
