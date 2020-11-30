package com.shawn.user.service;

import com.shawn.user.exception.ParticipateActivityException;
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

    DriverRepository driverRepository;

    PassengerRepository passengerRepository;

    ActivityRepository activityRepository;

    ActivityParticipantRepository activityParticipantRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder;

    UserLocationRepository userLocationRepository;

    public UserServiceImpl(DriverRepository driverRepository, PassengerRepository passengerRepository,
                           ActivityRepository activityRepository, ActivityParticipantRepository activityParticipantRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder, UserLocationRepository userLocationRepository) {
        this.driverRepository = driverRepository;
        this.passengerRepository = passengerRepository;
        this.activityRepository = activityRepository;
        this.activityParticipantRepository = activityParticipantRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userLocationRepository = userLocationRepository;
    }

    @Override
    public SignUpSuccessResponseDto signUpAsPassenger(SignUpFormDto signUpFormDto) {
        if (isPassengerExist(signUpFormDto)) {
            throw new SignUpException("Passenger already exist.");
        }
        String hashedPassword = bCryptPasswordEncoder.encode(signUpFormDto.getPassword());
        Passenger newPassenger = passengerRepository.save(Passenger.builder()
                .email(signUpFormDto.getEmail())
                .hashedPassword(hashedPassword)
                .name(signUpFormDto.getName())
                .build());
        return SignUpSuccessResponseDto.builder().id(newPassenger.getId()).email(newPassenger.getEmail()).name(newPassenger.getName()).build();
    }

    @Override
    public SignUpSuccessResponseDto signUpAsDriver(SignUpFormDto signUpFormDto) {
        if (isDriverExist(signUpFormDto)) {
            throw new SignUpException("Driver already exist.");
        }
        String hashedPassword = bCryptPasswordEncoder.encode(signUpFormDto.getPassword());
        Driver newDriver = driverRepository.save(Driver.builder()
                .email(signUpFormDto.getEmail())
                .hashedPassword(hashedPassword)
                .name(signUpFormDto.getName())
                .carType(signUpFormDto.getCarType())
                .build());
        return SignUpSuccessResponseDto.builder().id(newDriver.getId()).email(newDriver.getEmail()).name(newDriver.getName()).build();
    }

    private boolean isPassengerExist(SignUpFormDto signUpFormDto) {
        return Optional.ofNullable(passengerRepository.findByEmail(signUpFormDto.getEmail())).isPresent();
    }

    private boolean isDriverExist(SignUpFormDto signUpFormDto) {
        return Optional.ofNullable(driverRepository.findByEmail(signUpFormDto.getEmail())).isPresent();
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
    public void participateActivity(String activityName, long driverId) {
        Activity activity = activityRepository.findByName(activityName);
        if (Optional.ofNullable(activity).isPresent()) {
            if (!isAlreadyParticipate(activity, driverId)) {
                activityParticipantRepository.save(ActivityParticipant.builder()
                        .activity(activity.getId())
                        .participant(driverId)
                        .build());
            } else {
                throw new ParticipateActivityException("You have participated.");
            }
        } else {
            throw new ParticipateActivityException("Activity is not exist.");
        }
    }

    private boolean isAlreadyParticipate(Activity activity, long driverId) {
        return Optional.ofNullable(activityParticipantRepository.findByActivityAndParticipant(activity.getId(), driverId)).isPresent();
    }

}
