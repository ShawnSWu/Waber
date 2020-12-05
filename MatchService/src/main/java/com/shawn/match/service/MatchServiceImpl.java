package com.shawn.match.service;

import com.shawn.match.exception.NotMatchActivityException;
import com.shawn.match.exception.ParticipateActivityException;
import com.shawn.match.model.Activity;
import com.shawn.match.model.ActivityDriver;
import com.shawn.match.model.MatchTrip;
import com.shawn.match.model.dto.*;
import com.shawn.match.repostitory.ActivityDriverRepository;
import com.shawn.match.repostitory.ActivityRepository;
import com.shawn.match.repostitory.MatchTripRepository;
import com.shawn.match.utils.DateTimeUtils;
import com.shawn.match.utils.Haversine;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class MatchServiceImpl implements MatchService {

    private final ActivityRepository activityRepository;

    private final ActivityDriverRepository activityDriverRepository;

    private final MatchTripRepository matchTripRepository;

    private final RestTemplate restTemplate;

    private final String USER_SERVICE_API;

    private final int WAITING_CONFIRM_MATCHED = 1, SUCCESSFULLY_MATCHED = 2, DRIVER_CANCEL_MATCHED = 3, PASSENGER_CANCEL_MATCHED = 4;

    public MatchServiceImpl(ActivityRepository activityRepository, ActivityDriverRepository activityDriverRepository,
                            MatchTripRepository matchTripRepository, RestTemplate restTemplate, String USER_SERVICE_API) {
        this.activityRepository = activityRepository;
        this.activityDriverRepository = activityDriverRepository;
        this.matchTripRepository = matchTripRepository;
        this.restTemplate = restTemplate;
        this.USER_SERVICE_API = USER_SERVICE_API;
    }

    @Override
    public void participateActivity(String activityName, long driverId) {
        DriverDto driverDto = getDriver(driverId);

        Activity activity = activityRepository.findByName(activityName);
        if (Optional.ofNullable(activity).isPresent()) {
            if (!isAlreadyParticipate(activity, driverId)) {
                activityDriverRepository.save(ActivityDriver.builder()
                        .activity(activity.getId())
                        .driverId(driverId)
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
    public StartMatchResponse startMatch(MatchPreferredConditionDto matchPreferredConditionDto, long passengerId) {
        String preferActivity = matchPreferredConditionDto.getActivity();
        String preferCarType = matchPreferredConditionDto.getCarType();
        Activity activity = activityRepository.findByName(preferActivity);
        if (Optional.ofNullable(activity).isEmpty()) {
            throw new NotMatchActivityException("Not match activity");
        }
        List<ActivityDriver> activityDrivers = activityDriverRepository.findAllByActivityAndCarType(activity.getId(), preferCarType);
        UserLocationDto passengerLocation = getUserLocation(passengerId);
        ActivityDriver activityDriver = findMatchDriver(activityDrivers, passengerLocation);
        Date date = DateTimeUtils.getCurrentDate();
        Date time = DateTimeUtils.getCurrentTime();
        MatchTrip matchTrip = matchTripRepository.save(MatchTrip.builder()
                .driver(activityDriver.getDriverId())
                .passenger(passengerId)
                .startPositionLatitude(matchPreferredConditionDto.getDestinationLatitude())
                .startPositionLongitude(matchPreferredConditionDto.getDestinationLongitude())
                .destinationLatitude(matchPreferredConditionDto.getDestinationLatitude())
                .destinationLongitude(matchPreferredConditionDto.getDestinationLongitude())
                .activity(activityDriver.getActivity())
                .matchStatus(WAITING_CONFIRM_MATCHED)
                .date(date).time(time)
                .build());
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
                .passengerId(matchTrip.getPassenger()).driverName(driverName).build();
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

//    @Override
//    public MatchTripDto getMatchTrip(String matchId) {
//        MatchTrip matchTrip = matchTripRepository.findByMatchIdAndPassenger(matchId);
//        return MatchTripDto.builder()
//                .driver(matchTrip.getDriver()).passenger(matchTrip.getPassenger())
//                .startPositionLatitude(matchTrip.getStartPositionLatitude())
//                .startPositionLongitude(matchTrip.getStartPositionLongitude())
//                .destinationLatitude(matchTrip.getDestinationLatitude())
//                .destinationLongitude(matchTrip.getDestinationLongitude())
//                .date(DateTimeUtils.convertDateToText(matchTrip.getDate()))
//                .time(DateTimeUtils.convertTimeToText(matchTrip.getTime()))
//                .activity(matchTrip.getActivity()).matchId(matchTrip.getMatchId())
//                .build();
//    }

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
        return Optional.ofNullable(activityDriverRepository.findByActivityAndDriverId(activity.getId(), driverId)).isPresent();
    }


}
