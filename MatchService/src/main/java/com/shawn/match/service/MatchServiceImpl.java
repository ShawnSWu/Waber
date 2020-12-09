package com.shawn.match.service;

import com.shawn.match.exception.NotMatchActivityException;
import com.shawn.match.exception.ParticipateActivityException;
import com.shawn.match.model.entity.Activity;
import com.shawn.match.model.entity.ActivityDriver;
import com.shawn.match.model.entity.MatchTrip;
import com.shawn.match.model.dto.*;
import com.shawn.match.repostitory.ActivityDriverRepository;
import com.shawn.match.repostitory.ActivityRepository;
import com.shawn.match.repostitory.MatchTripRepository;
import com.shawn.match.utils.DateTimeUtils;
import com.shawn.match.utils.Haversine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class MatchServiceImpl implements MatchService {

    private final ActivityRepository activityRepository;

    private final ActivityDriverRepository activityDriverRepository;

    private final MatchTripRepository matchTripRepository;

    private final RestTemplate restTemplate;

    @Value("#{userServiceUrl}")
    private String USER_SERVICE_API;

    private final int WAITING_CONFIRM_MATCHED = 1, SUCCESSFULLY_MATCHED = 2, DRIVER_CANCEL_MATCHED = 3, PASSENGER_CANCEL_MATCHED = 4;

    public MatchServiceImpl(ActivityRepository activityRepository, ActivityDriverRepository activityDriverRepository,
                            MatchTripRepository matchTripRepository, RestTemplate restTemplate) {
        this.activityRepository = activityRepository;
        this.activityDriverRepository = activityDriverRepository;
        this.matchTripRepository = matchTripRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public void participateActivity(String activityName, long driverId) {
        DriverDto driverDto = getDriver(driverId);
        Activity activity = activityRepository.findByName(activityName);
        if (Optional.ofNullable(activity).isPresent()) {
            if (!isAlreadyParticipate(activity, driverId)) {
                activityDriverRepository.save(ActivityDriver.builder()
                        .activityId(activity.getId())
                        .driverId(driverDto.getId())
                        .carTypeId(driverDto.getCarTypeId())
                        .build());
            } else {
                throw new ParticipateActivityException("You have participated.");
            }
        } else {
            throw new ParticipateActivityException("Activity is not exist.");
        }
    }

    private DriverDto getDriver(long driverId) {
        return restTemplate.getForEntity(String.format("%s/api/users/%s", USER_SERVICE_API, driverId), DriverDto.class).getBody();
    }

    private UserLocationDto getUserLocation(long userId) {
        return restTemplate.getForEntity(String.format("%s/api/users/%s/location", USER_SERVICE_API, userId), UserLocationDto.class).getBody();
    }

    public CarTypeResponse getCarType(String carTypeName) {
        return restTemplate.getForEntity(String.format("%s/api/carType/type/%s", USER_SERVICE_API, carTypeName), CarTypeResponse.class).getBody();
    }


    @Override
    public StartMatchResponse startMatch(MatchPreferredConditionDto matchPreferredConditionDto, long passengerId) {
        Activity activity = activityRepository.findByName(matchPreferredConditionDto.getActivity());
        if (Optional.ofNullable(activity).isEmpty()) {
            throw new NotMatchActivityException("Not match activity");
        }
        CarTypeResponse carType = getCarType(matchPreferredConditionDto.getCarType());
        List<ActivityDriver> activityDrivers = activityDriverRepository.findAllByActivityIdAndCarTypeId(activity.getId(), carType.getId());
        UserLocationDto passengerLocation = getUserLocation(passengerId);
        ActivityDriver activityDriver = findMatchDriver(activityDrivers, passengerLocation);
        Date date = DateTimeUtils.getCurrentDate();
        Date time = DateTimeUtils.getCurrentTime();
        MatchTrip matchTrip = matchTripRepository.save(MatchTrip.builder()
                .driver(activityDriver.getDriverId()).passenger(passengerId)
                .startPositionLatitude(passengerLocation.getLatitude())
                .startPositionLongitude(passengerLocation.getLongitude())
                .activityId(activityDriver.getActivityId())
                .carTypeId(activityDriver.getCarTypeId()).matchStatus(WAITING_CONFIRM_MATCHED)
                .date(date).time(time).build());
        return StartMatchResponse.builder().matchId(matchTrip.getId()).build();
    }

    @Override
    public MatchedResultResponse getMatch(long passengerId, long matchId) {
        MatchTrip matchTrip = matchTripRepository.findByIdAndPassenger(matchId, passengerId);
        if (Optional.ofNullable(matchTrip).isEmpty()) {
            return MatchedResultResponse.builder().completed(false).build();
        }
        String driverName = getDriver(matchTrip.getDriver()).getName();
        return MatchedResultResponse.builder().completed(true)
                .id(matchId).driverId(matchTrip.getDriver())
                .date(DateTimeUtils.convertDateToText(matchTrip.getDate()))
                .time(DateTimeUtils.convertTimeToText(matchTrip.getTime()))
                .passengerId(matchTrip.getPassenger()).driverName(driverName)
                .startPositionLatitude(matchTrip.getStartPositionLatitude())
                .startPositionLongitude(matchTrip.getStartPositionLongitude())
                .activityId(matchTrip.getActivityId())
                .carTypeId(matchTrip.getCarTypeId()).build();
    }

    @Override
    public void confirmMatched(long matchId, long passengerId) {
        matchTripRepository.updateMatchedTripStatus(matchId, passengerId, SUCCESSFULLY_MATCHED);
    }

    @Override
    public void driverCancelMatched(long matchId, long passengerId) {
        matchTripRepository.updateMatchedTripStatus(matchId, passengerId, DRIVER_CANCEL_MATCHED);
    }

    @Override
    public void passengerCancelMatched(long matchId, long passengerId) {
        matchTripRepository.updateMatchedTripStatus(matchId, passengerId, PASSENGER_CANCEL_MATCHED);
    }

    @Override
    public ActivityResponse getActivity(long activityId) {
        Activity activity = activityRepository.findById(activityId);
        return ActivityResponse.builder()
                .id(activity.getId()).extraPrice(activity.getExtraPrice())
                .startDay(activity.getStartDay()).expireDay(activity.getExpireDay())
                .name(activity.getName()).build();
    }


    private ActivityDriver findMatchDriver(List<ActivityDriver> activityDrivers, UserLocationDto passengerLocation) {
        double minDistance = Double.MAX_VALUE;
        Optional<ActivityDriver> matchDriver = Optional.empty();
        for (ActivityDriver driver : activityDrivers) {
            UserLocationDto driverLocation = getUserLocation(driver.getDriverId());
            double distance = Haversine.distance(passengerLocation.getLatitude(), passengerLocation.getLongitude(), driverLocation.getLatitude(), driverLocation.getLongitude());
            if (distance < minDistance) {
                minDistance = distance;
                matchDriver = Optional.of(driver);
            }
        }
        return matchDriver.get();
    }

    private boolean isAlreadyParticipate(Activity activity, long driverId) {
        return Optional.ofNullable(activityDriverRepository.findByActivityIdAndDriverId(activity.getId(), driverId)).isPresent();
    }


}
