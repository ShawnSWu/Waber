package com.shawn.user.service;

import com.shawn.user.exception.SignUpException;
import com.shawn.user.model.*;
import com.shawn.user.model.dto.SignUpFormDto;
import com.shawn.user.model.dto.response.SignUpSuccessResponseDto;
import com.shawn.user.repostitory.*;
import com.shawn.user.utils.DateUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder;

    UserLocationRepository userLocationRepository;

    private final int DRIVER_ROLE_CODE = 1, PASSENGER_ROLE_CODE = 2;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, UserLocationRepository userLocationRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userLocationRepository = userLocationRepository;
    }

    @Override
    public SignUpSuccessResponseDto signUpAsPassenger(SignUpFormDto signUpFormDto) {
        if (isPassengerExist(signUpFormDto)) {
            throw new SignUpException("Passenger already exist.");
        }
        User newPassenger = createUser(signUpFormDto, PASSENGER_ROLE_CODE);
        return SignUpSuccessResponseDto.builder().id(newPassenger.getId()).email(newPassenger.getEmail()).name(newPassenger.getName()).build();
    }

    @Override
    public SignUpSuccessResponseDto signUpAsDriver(SignUpFormDto signUpFormDto) {
        if (isDriverExist(signUpFormDto)) {
            throw new SignUpException("Driver already exist.");
        }
        User newDriver = createUser(signUpFormDto, DRIVER_ROLE_CODE);
        return SignUpSuccessResponseDto.builder().id(newDriver.getId()).email(newDriver.getEmail()).name(newDriver.getName()).build();
    }

    private User createUser(SignUpFormDto signUpFormDto, int userRole) {
        String hashedPassword = bCryptPasswordEncoder.encode(signUpFormDto.getPassword());
        return userRepository.save(User.builder()
                .email(signUpFormDto.getEmail())
                .hashedPassword(hashedPassword)
                .name(signUpFormDto.getName())
                .role(userRole)
                .build());
    }

    private boolean isPassengerExist(SignUpFormDto signUpFormDto) {
        return Optional.ofNullable(userRepository.findByEmailAndRole(signUpFormDto.getEmail(), PASSENGER_ROLE_CODE)).isPresent();
    }

    private boolean isDriverExist(SignUpFormDto signUpFormDto) {
        return Optional.ofNullable(userRepository.findByEmailAndRole(signUpFormDto.getEmail(), DRIVER_ROLE_CODE)).isPresent();
    }

    @Override
    public void updateLocation(long userId, double latitude, double longitude) {
        userLocationRepository.save(UserLocation.builder()
                .userId(userId)
                .datetime(DateUtils.asDate(LocalDateTime.now()))
                .latitude(latitude)
                .longitude(longitude)
                .build());
    }

}
