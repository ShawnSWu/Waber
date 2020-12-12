package com.shawn.user.service;

import com.shawn.user.exception.*;
import com.shawn.user.model.dto.*;
import com.shawn.user.model.entity.*;
import com.shawn.user.repostitory.*;
import com.shawn.user.utils.DateUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final ActivityRepository activityRepository;

    private final ActivityDriverRepository activityDriverRepository;

    private final UserRepository userRepository;

    private final CarTypeRepository carTypeRepository;

    private final DriverCarTypeRepository driverCarTypeRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserLocationRepository userLocationRepository;

    private final int DRIVER_ROLE_CODE = 1, PASSENGER_ROLE_CODE = 2;

    public UserServiceImpl(ActivityRepository activityRepository, ActivityDriverRepository activityDriverRepository,
                           UserRepository userRepository, CarTypeRepository carTypeRepository, DriverCarTypeRepository driverCarTypeRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder, UserLocationRepository userLocationRepository) {
        this.activityRepository = activityRepository;
        this.activityDriverRepository = activityDriverRepository;
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
            throw new NotFoundCarTypeException(String.format("Not found car type name: %s", signUpReq.getCarType()));
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
        return userRepository.findByEmailAndRole(signUpReq.getEmail(), PASSENGER_ROLE_CODE).isPresent();
    }

    private boolean isDriverExist(SignUpReq signUpReq) {
        return userRepository.findByEmailAndRole(signUpReq.getEmail(), DRIVER_ROLE_CODE).isPresent();
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
        UserLocation userLocation = userLocationRepository.findFirstByUserIdOrderByIdDesc(userId)
                .orElseThrow(()->new NotFoundUserLocationException(String.format("id: %d", userId)));
        return UserLocationDto.builder().userId(userId).latitude(userLocation.getLatitude()).longitude(userLocation.getLongitude()).build();
    }

    @Override
    public DriverDto getDriver(long driverId) {
        User driver = userRepository.findByIdAndRole(driverId, DRIVER_ROLE_CODE)
                .orElseThrow(() -> new NotFoundDriverException(String.format("id: %d", driverId)));
        DriverCarType driverSupportCarType = driverCarTypeRepository.findByDriver(driver.getId()).orElseThrow(NotFoundDriverCarTypeException::new);
        return DriverDto.builder().id(driverSupportCarType.getDriver())
                .email(driver.getEmail()).name(driver.getName())
                .carTypeId(driverSupportCarType.getCarTypeId()).build();
    }

    @Override
    public CarTypeDto getCarType(long carTypeId) {
        CarType carType = carTypeRepository.findById(carTypeId)
                .orElseThrow(() -> new NotFoundCarTypeException(String.format("id: %d", carTypeId)));
        return CarTypeDto.builder()
                .id(carType.getId()).type(carType.getType())
                .extraPrice(carType.getExtraPrice()).build();
    }

    @Override
    public CarTypeDto getCarType(String carTypeName) {
        CarType carType = carTypeRepository.findByType(carTypeName)
                .orElseThrow(() -> new NotFoundCarTypeException(String.format("name: %s", carTypeName)));
        return CarTypeDto.builder()
                .id(carType.getId()).type(carType.getType())
                .extraPrice(carType.getExtraPrice()).build();
    }

    @Override
    public void participateActivity(String activityName, long driverId) {
        DriverDto driverDto = getDriver(driverId);
        ActivityResponse activity = getActivityByName(activityName);
        if (!isAlreadyParticipate(activity.getId(), driverId)) {
            activityDriverRepository.save(ActivityDriver.builder()
                    .activityId(activity.getId())
                    .driverId(driverDto.getId())
                    .carTypeId(driverDto.getCarTypeId())
                    .build());
        } else {
            throw new ParticipateActivityException("You have participated.");
        }
    }

    @Override
    public ActivityResponse getActivityByName(String activityName) {
        Activity activity = activityRepository.findByName(activityName).orElseThrow(() -> new ParticipateActivityException("Activity is not exist."));
        return ActivityResponse.builder().id(activity.getId())
                .name(activity.getName()).extraPrice(activity.getExtraPrice())
                .startDay(activity.getStartDay()).expireDay(activity.getExpireDay())
                .build();
    }

    private boolean isAlreadyParticipate(long activityId, long driverId) {
        return activityDriverRepository.findByActivityIdAndDriverId(activityId, driverId).isPresent();
    }

    @Override
    public ActivityResponse getActivityById(long activityId) {
        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new ParticipateActivityException("Activity is not exist."));
        return ActivityResponse.builder()
                .id(activity.getId()).extraPrice(activity.getExtraPrice())
                .startDay(activity.getStartDay()).expireDay(activity.getExpireDay())
                .name(activity.getName()).build();
    }

    @Override
    public List<DriverDto> getDriverByActivityAndCarType(long activityId, long carTypeId) {
        List<ActivityDriver> activities = activityDriverRepository.findAllByActivityIdAndCarTypeId(activityId, carTypeId);
        List<DriverDto> drivers = new ArrayList<>();
        for (ActivityDriver activityDriver : activities) {
            DriverDto driver = getDriver(activityDriver.getDriverId());
            drivers.add(DriverDto.builder().id(driver.getId()).name(driver.getName())
                    .email(driver.getEmail()).carTypeId(driver.getCarTypeId()).build());
        }
        return drivers;
    }

}
