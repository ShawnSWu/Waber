package com.shawn.user.service;

import com.shawn.user.exception.FindCarTypeException;
import com.shawn.user.exception.SignUpException;
import com.shawn.user.model.dto.SignUpReq;
import com.shawn.user.model.dto.CarTypeDto;
import com.shawn.user.model.dto.DriverDto;
import com.shawn.user.model.dto.SignUpSuccessResponse;
import com.shawn.user.model.dto.UserLocationDto;
import com.shawn.user.model.entity.CarType;
import com.shawn.user.model.entity.DriverCarType;
import com.shawn.user.model.entity.User;
import com.shawn.user.model.entity.UserLocation;
import com.shawn.user.repostitory.*;
import com.shawn.user.utils.DateUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final CarTypeRepository carTypeRepository;

    private final DriverCarTypeRepository driverCarTypeRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserLocationRepository userLocationRepository;

    private final int DRIVER_ROLE_CODE = 1, PASSENGER_ROLE_CODE = 2;

    public UserServiceImpl(UserRepository userRepository, CarTypeRepository carTypeRepository, DriverCarTypeRepository driverCarTypeRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder, UserLocationRepository userLocationRepository) {
        this.userRepository = userRepository;
        this.carTypeRepository = carTypeRepository;
        this.driverCarTypeRepository = driverCarTypeRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userLocationRepository = userLocationRepository;
    }

    @Override
    public SignUpSuccessResponse signUpAsPassenger(SignUpReq signUpReq) {
        if (isPassengerExist(signUpReq)) {
            throw new SignUpException("Passenger already exist.");
        }
        User newPassenger = createPassengerUser(signUpReq);
        return SignUpSuccessResponse.builder().id(newPassenger.getId()).email(newPassenger.getEmail()).name(newPassenger.getName()).build();
    }

    private User createPassengerUser(SignUpReq signUpReq) {
        return userRepository.save(User.builder()
                .email(signUpReq.getEmail())
                .hashedPassword(bCryptPasswordEncoder.encode(signUpReq.getPassword()))
                .name(signUpReq.getName())
                .role(PASSENGER_ROLE_CODE)
                .build());
    }

    @Override
    public SignUpSuccessResponse signUpAsDriver(SignUpReq signUpReq) {
        if (isDriverExist(signUpReq)) {
            throw new SignUpException("Driver already exist.");
        }
        User newDriver = createDriverUser(signUpReq);
        return SignUpSuccessResponse.builder().id(newDriver.getId()).email(newDriver.getEmail()).name(newDriver.getName()).build();
    }

    private User createDriverUser(SignUpReq signUpReq) {
        String hashedPassword = bCryptPasswordEncoder.encode(signUpReq.getPassword());
        Optional<CarType> carType = carTypeRepository.findByType(signUpReq.getCarType());
        if (carType.isEmpty()) {
            throw new FindCarTypeException(String.format("Not found car type name: %s", signUpReq.getCarType()));
        }
        User user = userRepository.save(User.builder()
                .email(signUpReq.getEmail())
                .hashedPassword(hashedPassword)
                .name(signUpReq.getName())
                .role(DRIVER_ROLE_CODE)
                .build());
        DriverCarType driverCarType = DriverCarType.builder()
                .driver(user.getId())
                .carTypeId(carType.get().getId())
                .build();
        driverCarTypeRepository.save(driverCarType);
        return user;
    }

    private boolean isPassengerExist(SignUpReq signUpReq) {
        return Optional.ofNullable(userRepository.findByEmailAndRole(signUpReq.getEmail(), PASSENGER_ROLE_CODE)).isPresent();
    }

    private boolean isDriverExist(SignUpReq signUpReq) {
        return Optional.ofNullable(userRepository.findByEmailAndRole(signUpReq.getEmail(), DRIVER_ROLE_CODE)).isPresent();
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
        return DriverDto.builder().id(driverSupportCarType.getDriver())
                .email(driver.getEmail()).name(driver.getName())
                .carTypeId(driverSupportCarType.getCarTypeId()).build();
    }

    @Override
    public CarTypeDto getCarType(long carTypeId) {
        Optional<CarType> carType = carTypeRepository.findById(carTypeId);
        if (carType.isEmpty()) {
            throw new FindCarTypeException(String.format("Not found car type id: %d", carTypeId));
        }
        return CarTypeDto.builder()
                .id(carType.get().getId()).type(carType.get().getType())
                .extraPrice(carType.get().getExtraPrice()).build();
    }

    @Override
    public CarTypeDto getCarType(String carTypeName) {
        Optional<CarType> carType = carTypeRepository.findByType(carTypeName);
        if (carType.isEmpty()) {
            throw new FindCarTypeException(String.format("Not found car type name: %s", carTypeName));
        }
        return CarTypeDto.builder()
                .id(carType.get().getId()).type(carType.get().getType())
                .extraPrice(carType.get().getExtraPrice()).build();
    }


}
