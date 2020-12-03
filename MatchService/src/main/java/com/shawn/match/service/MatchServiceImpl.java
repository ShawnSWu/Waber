package com.shawn.match.service;

import com.shawn.match.exception.NotMatchActivityException;
import com.shawn.match.exception.ParticipateActivityException;
import com.shawn.match.model.Activity;
import com.shawn.match.model.ActivityDriver;
import com.shawn.match.model.WaitingMatchPassenger;
import com.shawn.match.model.dto.response.*;
import com.shawn.match.repostitory.ActivityDriverRepository;
import com.shawn.match.repostitory.ActivityRepository;
import com.shawn.match.repostitory.WaitingMatchPassengerRepository;
import com.shawn.match.utils.Haversine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MatchServiceImpl implements MatchService {

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    ActivityDriverRepository activityDriverRepository;

    @Autowired
    WaitingMatchPassengerRepository waitingMatchPassengerRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    String USER_SERVICE_API;


    @Override
    public void participateActivity(String activityName, long driverId) {
        DriverDto driverDto = getDriver(driverId);

        Activity activity = activityRepository.findByName(activityName);
        if (Optional.ofNullable(activity).isPresent()) {
            if (!isAlreadyParticipate(activity, driverId)) {
                activityDriverRepository.save(ActivityDriver.builder()
                        .activity(activity.getId())
                        .driver(driverId)
                        .carType(driverDto.getCarType())
                        .build());
            } else {
                throw new ParticipateActivityException("You have participated.");
            }
        } else {
            throw new ParticipateActivityException("Activity is not exist.");
        }
    }

    private DriverDto getDriver(long driverId) {
        return restTemplate.getForEntity(USER_SERVICE_API + "/api/users/" + driverId, DriverDto.class).getBody();
    }

    private UserLocationDto getUserLocation(long userId) {
        return restTemplate.getForEntity(USER_SERVICE_API + "/api/users/" + userId + "/location", UserLocationDto.class).getBody();
    }


    @Override
    public WaitingMatchDto startMatch(MatchPreferredConditionDto matchPreferredConditionDto, long passengerId) {
        String preferActivity = matchPreferredConditionDto.getActivity();
        String preferCarType = matchPreferredConditionDto.getCarType();
        Activity activity = activityRepository.findByName(preferActivity);
        if (Optional.ofNullable(activity).isEmpty()) {
            throw new NotMatchActivityException("Not match activity");
        }
        WaitingMatchPassenger waitingMatchPassenger = waitingMatchPassengerRepository.save(
                WaitingMatchPassenger
                        .builder()
                        .passenger(passengerId)
                        .preferActivity(activity.getId())
                        .preferCarType(preferCarType)
                        .build());

        return WaitingMatchDto.builder().passengerId(waitingMatchPassenger.getId()).id(waitingMatchPassenger.getId()).build();
    }

    @Override
    public MatchedResultDto getMatch(long passengerId, long matchId) {
        WaitingMatchPassenger waitingMatchPassenger = waitingMatchPassengerRepository.findByPassengerAndId(passengerId, matchId);
        List<ActivityDriver> activityDrivers = activityDriverRepository.findAllByActivityAndCarType(waitingMatchPassenger.getPreferActivity(), waitingMatchPassenger.getPreferCarType());
        if (activityDrivers.isEmpty()) {
            return MatchedResultDto.builder().completed(false).build();
        }
        UserLocationDto passengerLocation = getUserLocation(passengerId);
        ActivityDriver activityDriver = findMatchDriver(activityDrivers, passengerLocation);
        DriverDto driverDto = getDriver(activityDriver.getId());
        return MatchedResultDto.builder().completed(true).driverId(driverDto.getId()).driverName(driverDto.getName()).passengerId(passengerId).build();
    }

    private ActivityDriver findMatchDriver(List<ActivityDriver> activityDrivers, UserLocationDto passengerLocation) {
        double minDistance = Double.MAX_VALUE;
        Optional<ActivityDriver> matchDriver = Optional.empty();
        for (ActivityDriver driver : activityDrivers) {
            UserLocationDto driverLocation = getUserLocation(driver.getId());
            double distance = Haversine.distance(passengerLocation.getLatitude(), passengerLocation.getLongitude(), driverLocation.getLatitude(), driverLocation.getLongitude());
            System.out.println(passengerLocation.getLatitude()+","+passengerLocation.getLongitude()+","+driverLocation.getLatitude()+","+driverLocation.getLongitude());
            System.out.println(driver.getDriver());
            System.out.println(distance);
            if (distance < minDistance) {
                minDistance = distance;
                matchDriver = Optional.of(driver);
            }
        }
        return matchDriver.get();
    }

    private boolean isAlreadyParticipate(Activity activity, long driverId) {
        return Optional.ofNullable(activityDriverRepository.findByActivityAndDriver(activity.getId(), driverId)).isPresent();
    }


}
