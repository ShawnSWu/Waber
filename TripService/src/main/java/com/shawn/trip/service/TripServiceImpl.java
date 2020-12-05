package com.shawn.trip.service;

import com.shawn.trip.model.Trip;
import com.shawn.trip.model.dto.MatchedTripDto;
import com.shawn.trip.model.dto.PickUpResponse;
import com.shawn.trip.repository.TripRepository;
import com.shawn.trip.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;

    private final RestTemplate restTemplate;

    public TripServiceImpl(TripRepository tripRepository, RestTemplate restTemplate) {
        this.tripRepository = tripRepository;
        this.restTemplate = restTemplate;
    }

    @Value("#{matchServiceUrl}")
    String MATCH_SERVICE_API;

    private final int ON_THE_WAY = 1, ARRIVE_DESTINATION = 2, CANCEL_TRIP_HALFWAY = 3;

    @Override
    public PickUpResponse pickUpTrip(long passengerId, long matchId) {
        modifyMatchStatusToAccept(passengerId, matchId);
        MatchedTripDto matchedTripDto = getMatchTrip(matchId, passengerId);
        Trip trip = tripRepository.save(Trip.builder()
                .driverId(matchedTripDto.getDriverId())
                .passengerId(matchedTripDto.getPassengerId())
                .matchId(matchId)
                .startPositionLatitude(matchedTripDto.getStartPositionLatitude())
                .startPositionLongitude(matchedTripDto.getStartPositionLongitude())
                .destinationLatitude(matchedTripDto.getDestinationLatitude())
                .destinationLongitude(matchedTripDto.getStartPositionLongitude())
                .date(DateTimeUtils.convertToDate(matchedTripDto.getDate()))
                .time(DateTimeUtils.convertToTime(matchedTripDto.getTime()))
                .tripStatus(ON_THE_WAY)
                .build());
        return PickUpResponse.builder().id(trip.getId()).build();
    }

    private void modifyMatchStatusToAccept(long passenger, long matchId) {
        restTemplate.put(String.format("%s/api/users/%d/match/%d/accept", MATCH_SERVICE_API, passenger, matchId), Void.class);
    }

    private MatchedTripDto getMatchTrip(long matchId, long passengerId) {
        return restTemplate.getForEntity(String.format("%s/api/users/%d/match/%d", MATCH_SERVICE_API, passengerId, matchId), MatchedTripDto.class).getBody();
    }

    @Override
    public void arriveDestination(long passengerId, long matchId, long tripId) {
        tripRepository.updateTripStatus(tripId, ARRIVE_DESTINATION);
    }

}
