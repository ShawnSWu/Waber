package com.shawn.user.service;

import com.shawn.user.exception.SignUpException;
import com.shawn.user.model.*;
import com.shawn.user.model.dto.SignUpFormDto;
import com.shawn.user.model.dto.response.DriverDto;
import com.shawn.user.model.dto.response.SignUpSuccessResponseDto;
import com.shawn.user.model.dto.response.UserLocationDto;
import com.shawn.user.repostitory.*;
import com.shawn.user.utils.DateUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final DriverCarTypeRepository driverCarTypeRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserLocationRepository userLocationRepository;

    private final int DRIVER_ROLE_CODE = 1, PASSENGER_ROLE_CODE = 2;

    public UserServiceImpl(UserRepository userRepository, DriverCarTypeRepository driverCarTypeRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder, UserLocationRepository userLocationRepository) {
        this.userRepository = userRepository;
        this.driverCarTypeRepository = driverCarTypeRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userLocationRepository = userLocationRepository;
    }

    @Override
    public SignUpSuccessResponseDto signUpAsPassenger(SignUpFormDto signUpFormDto) {
        if (isPassengerExist(signUpFormDto)) {
            throw new SignUpException("Passenger already exist.");
        }
        User newPassenger = createPassengerUser(signUpFormDto);
        return SignUpSuccessResponseDto.builder().id(newPassenger.getId()).email(newPassenger.getEmail()).name(newPassenger.getName()).build();
    }

    private User createPassengerUser(SignUpFormDto signUpFormDto) {
        return userRepository.save(User.builder()
                .email(signUpFormDto.getEmail())
                .hashedPassword(bCryptPasswordEncoder.encode(signUpFormDto.getPassword()))
                .name(signUpFormDto.getName())
                .role(PASSENGER_ROLE_CODE)
                .build());
    }

    @Override
    public SignUpSuccessResponseDto signUpAsDriver(SignUpFormDto signUpFormDto) {
        if (isDriverExist(signUpFormDto)) {
            throw new SignUpException("Driver already exist.");
        }
        User newDriver = createDriverUser(signUpFormDto);
        return SignUpSuccessResponseDto.builder().id(newDriver.getId()).email(newDriver.getEmail()).name(newDriver.getName()).build();
    }

    private User createDriverUser(SignUpFormDto signUpFormDto) {
        String hashedPassword = bCryptPasswordEncoder.encode(signUpFormDto.getPassword());
        User user = userRepository.save(User.builder()
                .email(signUpFormDto.getEmail())
                .hashedPassword(hashedPassword)
                .name(signUpFormDto.getName())
                .role(DRIVER_ROLE_CODE)
                .build());
        DriverCarType driverCarType = DriverCarType.builder()
                .driver(user.getId())
                .carType(signUpFormDto.getCarType())
                .build();
        driverCarTypeRepository.save(driverCarType);
        return user;
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

    @Override
    public UserLocationDto getUserLatestLocation(long userId) {
        UserLocation userLocation = userLocationRepository.findFirstByUserIdOrderByIdDesc(userId);
        return UserLocationDto.builder().userId(userId).latitude(userLocation.getLatitude()).longitude(userLocation.getLongitude()).build();
    }

    @Override
    public DriverDto getDriver(long driverId) {
        User driver = userRepository.findByIdAndRole(driverId, DRIVER_ROLE_CODE);
        DriverCarType driverSupportCarType = driverCarTypeRepository.findByDriver(driverId);
        return DriverDto.builder().id(driver.getId()).email(driver.getEmail()).name(driver.getName()).carType(driverSupportCarType.getCarType()).build();
    }


}
