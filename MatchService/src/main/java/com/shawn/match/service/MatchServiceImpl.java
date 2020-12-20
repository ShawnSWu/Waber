package com.shawn.match.service;

import com.shawn.match.exception.MatchIdNotFoundException;
import com.shawn.match.exception.NotMatchActivityException;
import com.shawn.match.exception.NotMatchCarTypeException;
import com.shawn.match.model.entity.MatchTrip;
import com.shawn.match.model.dto.*;
import com.shawn.match.repostitory.MatchTripRepository;
import com.shawn.match.utils.Haversine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.shawn.match.utils.DateTimeUtils.*;
import static com.shawn.match.utils.DateTimeUtils.getCurrentDate;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchTripRepository matchTripRepository;

    private final RestTemplate restTemplate;

    @Value("${user.service.rul}")
    private String USER_SERVICE_API;

    private final int WAITING_CONFIRM_MATCHED = 1, SUCCESSFULLY_MATCHED = 2, DRIVER_CANCEL_MATCHED = 3,
            PASSENGER_CANCEL_MATCHED = 4, NOT_YET_MATCHED = -1;

    public MatchServiceImpl(MatchTripRepository matchTripRepository, RestTemplate restTemplate) {
        this.matchTripRepository = matchTripRepository;
        this.restTemplate = restTemplate;
    }

    private DriverDto[] findMatchDriverByCondition(long activityId, long carTypeId) {
        return restTemplate.getForEntity(String.format("%s/api/activities/%d/carType/%d", USER_SERVICE_API, activityId, carTypeId), DriverDto[].class).getBody();
    }

    private UserLocationDto getUserLocation(long userId) {
        return restTemplate.getForEntity(String.format("%s/api/users/%s/location", USER_SERVICE_API, userId), UserLocationDto.class).getBody();
    }

    private Optional<ActivityResponse> getActivityByName(String activityName) {
        ResponseEntity<ActivityResponse> responseEntity = restTemplate.getForEntity(String.format("%s/api/activities/name/%s", USER_SERVICE_API, activityName), ActivityResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
            return Optional.empty();
        }
        return Optional.ofNullable(responseEntity.getBody());
    }

    private Optional<CarTypeResponse> getCarTypeByName(String carTypeName) {
        ResponseEntity<CarTypeResponse> responseEntity = restTemplate.getForEntity(String.format("%s/api/carType/name/%s", USER_SERVICE_API, carTypeName), CarTypeResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
            return Optional.empty();
        }
        return Optional.ofNullable(responseEntity.getBody());
    }

    private DriverDto getDriver(long driverId) {
        return restTemplate.getForEntity(String.format("%s/api/users/%d", USER_SERVICE_API, driverId), DriverDto.class).getBody();
    }

    @Override
    public StartMatchResponse startMatch(MatchPreferredConditionDto preferredCondition, long passengerId) {
        ActivityResponse activityResponse = getActivityByName(preferredCondition.getActivityName()).orElseThrow(NotMatchActivityException::new);
        CarTypeResponse carTypeResponse = getCarTypeByName(preferredCondition.getCarType()).orElseThrow(NotMatchCarTypeException::new);
        LocationDto destinationLocation = preferredCondition.getStartLocation();
        MatchTrip matchTrip = tryMatch(passengerId, activityResponse.getId(), carTypeResponse.getId(),
                destinationLocation.getLatitude(), destinationLocation.getLongitude());
        return StartMatchResponse.builder().matchId(matchTrip.getId()).build();
    }

    private boolean hasMatchDrivers(DriverDto[] matchDrivers) {
        return matchDrivers.length != 0;
    }

    private MatchTrip tryMatch(long passengerId, long activityId, long carTypeId, double destinationLatitude, double destinationLongitude) {
        MatchTrip matchTrip;
        DriverDto[] matchDrivers = findMatchDriverByCondition(activityId, carTypeId);
        UserLocationDto passengerLocation = getUserLocation(passengerId);
        Date date = getCurrentDate();
        Date time = getCurrentTime();
        if (hasMatchDrivers(matchDrivers)) {
            MatchDriverDto activityDriver = findMostCloseDriver(matchDrivers, passengerLocation);
            matchTrip = matchTripRepository.save(MatchTrip.builder()
                    .driver(activityDriver.getDriver().getId()).passenger(passengerId)
                    .startPositionLatitude(passengerLocation.getLatitude())
                    .startPositionLongitude(passengerLocation.getLongitude())
                    .destinationPositionLatitude(destinationLatitude)
                    .destinationPositionLongitude(destinationLongitude)
                    .activityId(activityId).distance(activityDriver.getDistance())
                    .carTypeId(activityDriver.getDriver().getCarTypeId()).matchStatus(WAITING_CONFIRM_MATCHED)
                    .date(date).time(time).build());
        } else {
            matchTrip = matchTripRepository.save(MatchTrip.builder()
                    .driver(NOT_YET_MATCHED).passenger(passengerId)
                    .startPositionLatitude(passengerLocation.getLatitude())
                    .startPositionLongitude(passengerLocation.getLongitude())
                    .destinationPositionLatitude(destinationLatitude)
                    .destinationPositionLongitude(destinationLongitude)
                    .activityId(activityId)
                    .carTypeId(NOT_YET_MATCHED).matchStatus(WAITING_CONFIRM_MATCHED)
                    .date(date).time(time).build());
        }
        return matchTrip;
    }

    @Override
    public MatchedResultResponse getMatch(long passengerId, long matchId) {
        MatchTrip matchTrip = matchTripRepository.findByIdAndPassenger(matchId, passengerId)
                .orElseThrow(() -> new MatchIdNotFoundException(matchId));
        MatchedResultResponse matchedResultResponse = MatchedResultResponse.builder().build();
        if (matchTrip.getDriver() == NOT_YET_MATCHED) {
            matchedResultResponse.setCompleted(false);
            tryMatch(passengerId, matchTrip.getActivityId(), matchTrip.getCarTypeId(),
                    matchTrip.getDestinationPositionLatitude(), matchTrip.getDestinationPositionLongitude());
        } else {
            DriverDto driverName = getDriver(matchTrip.getDriver());
            matchedResultResponse = MatchedResultResponse.builder().completed(true)
                    .id(matchId).driver(driverName).passengerId(matchTrip.getPassenger())
                    .date(convertDateToText(matchTrip.getDate())).time(convertTimeToText(matchTrip.getTime()))
                    .startPositionLatitude(matchTrip.getStartPositionLatitude())
                    .startPositionLongitude(matchTrip.getStartPositionLongitude())
                    .destinationLatitude(matchTrip.getDestinationPositionLatitude())
                    .destinationLongitude(matchTrip.getDestinationPositionLongitude())
                    .activityId(matchTrip.getActivityId()).carTypeId(matchTrip.getCarTypeId()).build();
        }
        return matchedResultResponse;
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

    private MatchDriverDto findMostCloseDriver(DriverDto[] matchDrivers, UserLocationDto passengerLocation) {
        double minDistance = Double.MAX_VALUE;
        Optional<DriverDto> matchDriver = Optional.empty();
        for (DriverDto driver : matchDrivers) {
            UserLocationDto driverLocation = getUserLocation(driver.getId());
            double distance = Haversine.distance(passengerLocation.getLatitude(), passengerLocation.getLongitude(), driverLocation.getLatitude(), driverLocation.getLongitude());
            if (distance < minDistance) {
                minDistance = distance;
                matchDriver = Optional.of(driver);
            }
        }
        return MatchDriverDto.builder().distance((int) minDistance).driver(matchDriver.get()).build();
    }


}
