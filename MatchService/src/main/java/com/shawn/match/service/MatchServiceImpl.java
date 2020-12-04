package com.shawn.match.service;

import com.shawn.match.exception.NotMatchActivityException;
import com.shawn.match.exception.ParticipateActivityException;
import com.shawn.match.model.Activity;
import com.shawn.match.model.ActivityDriver;
import com.shawn.match.model.MatchTrip;
import com.shawn.match.model.dto.response.*;
import com.shawn.match.repostitory.ActivityDriverRepository;
import com.shawn.match.repostitory.ActivityRepository;
import com.shawn.match.repostitory.MatchTripRepository;
import com.shawn.match.utils.Haversine;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MatchServiceImpl implements MatchService {

    private ActivityRepository activityRepository;

    private ActivityDriverRepository activityDriverRepository;

    private MatchTripRepository matchTripRepository;

    private RestTemplate restTemplate;

    private String USER_SERVICE_API;

    private Map<String, WaitingMatchPassengerDto> waitingMatchGroup = new HashMap<>();

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
    public WaitingMatchResponseDto startMatch(MatchPreferredConditionDto matchPreferredConditionDto, long passengerId) {
        String preferActivity = matchPreferredConditionDto.getActivity();
        String preferCarType = matchPreferredConditionDto.getCarType();
        Activity activity = activityRepository.findByName(preferActivity);
        if (Optional.ofNullable(activity).isEmpty()) {
            throw new NotMatchActivityException("Not match activity");
        }
        WaitingMatchPassengerDto waitingMatchPassengerDto = WaitingMatchPassengerDto.builder()
                .passenger(passengerId)
                .preferActivity(activity.getId())
                .preferCarType(preferCarType)
                .build();
        String matchId = createWaitingMatchId(passengerId);
        waitingMatchGroup.put(matchId, waitingMatchPassengerDto);
        return WaitingMatchResponseDto.builder().passengerId(passengerId).id(matchId).build();
    }

    private String createWaitingMatchId(long passengerId) {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
        return String.format("M%s%sP%d", date, time, passengerId);
    }

    @Override
    public MatchedResultResponseDto getMatch(long passengerId, String matchId) {
        WaitingMatchPassengerDto passengerPreferredCondition = waitingMatchGroup.get(matchId);
        List<ActivityDriver> activityDrivers = activityDriverRepository.findAllByActivityAndCarType(passengerPreferredCondition.getPreferActivity(), passengerPreferredCondition.getPreferCarType());
        if (activityDrivers.isEmpty()) {
            return MatchedResultResponseDto.builder().completed(false).build();
        }
        UserLocationDto passengerLocation = getUserLocation(passengerPreferredCondition.getPassenger());
        ActivityDriver activityDriver = findMatchDriver(activityDrivers, passengerLocation);
        DriverDto driverDto = getDriver(activityDriver.getDriverId());

        MatchTrip matchTrip = MatchTrip.builder()
                .driver(activityDriver.getDriverId())
                .passenger(passengerId)
                .activity(activityDriver.getActivity())
                .matchStatus(WAITING_CONFIRM_MATCHED)
                .build();
        matchTripRepository.save(matchTrip);

        return MatchedResultResponseDto.builder().completed(true).driverId(driverDto.getId()).driverName(driverDto.getName()).passengerId(passengerId).build();
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
        return Optional.ofNullable(activityDriverRepository.findByActivityAndDriverId(activity.getId(), driverId)).isPresent();
    }


}
