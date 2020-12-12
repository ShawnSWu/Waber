package com.shawn.trip.service;

import com.shawn.trip.exception.NotFoundTripException;
import com.shawn.trip.model.entity.Trip;
import com.shawn.trip.model.dto.ArriveDestinationReq;
import com.shawn.trip.model.dto.MatchTripResponse;
import com.shawn.trip.model.dto.PickUpResponse;
import com.shawn.trip.model.dto.TripResponse;
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

    @Value("${trip.service.url}")
    String MATCH_SERVICE_API;

    private final int ON_THE_WAY = 1, ARRIVE_DESTINATION = 2, CANCEL_TRIP_HALFWAY = 3;

    @Override
    public PickUpResponse pickUpTrip(long passengerId, long matchId) {
        final int INITIAL_DISTANCE_ZERO = 0;
        modifyMatchStatusToAccept(passengerId, matchId);
        MatchTripResponse matchTripResponse = getMatchTrip(matchId, passengerId);
        Trip trip = tripRepository.save(Trip.builder()
                .matchId(matchTripResponse.getId())
                .date(DateTimeUtils.convertToDate(matchTripResponse.getDate()))
                .time(DateTimeUtils.convertToTime(matchTripResponse.getTime()))
                .tripStatus(ON_THE_WAY)
                .tripDistance(INITIAL_DISTANCE_ZERO)
                .build());
        return PickUpResponse.builder().id(trip.getId()).build();
    }

    private void modifyMatchStatusToAccept(long passenger, long matchId) {
        restTemplate.put(String.format("%s/api/users/%d/match/%d/accept", MATCH_SERVICE_API, passenger, matchId), Void.class);
    }

    private MatchTripResponse getMatchTrip(long matchId, long passengerId) {
        return restTemplate.getForEntity(String.format("%s/api/users/%d/match/%d", MATCH_SERVICE_API, passengerId, matchId), MatchTripResponse.class).getBody();
    }

    @Override
    public void arriveDestination(long passengerId, long matchId, long tripId, ArriveDestinationReq arriveDestinationReq) {
        long tripDistance = arriveDestinationReq.getDistance();
        double destinationLatitude = arriveDestinationReq.getLocationDto().getLatitude();
        double destinationLongitude = arriveDestinationReq.getLocationDto().getLongitude();
        tripRepository.updateTripStatus(tripId, ARRIVE_DESTINATION, tripDistance, destinationLatitude, destinationLongitude);
    }

    @Override
    public TripResponse getTrip(long passengerId, long matchId, long tripId) {
        Trip trip = tripRepository.findByIdAndMatchId(tripId, matchId)
                .orElseThrow(() -> new NotFoundTripException(String.format("id: %d", tripId)));
        MatchTripResponse matchTripResponse = getMatchTrip(matchId, passengerId);
        return TripResponse.builder()
                .id(trip.getId())
                .driverId(matchTripResponse.getDriverId())
                .matchId(trip.getMatchId())
                .passengerId(matchTripResponse.getPassengerId())
                .startPositionLatitude(matchTripResponse.getStartPositionLatitude())
                .startPositionLongitude(matchTripResponse.getStartPositionLongitude())
                .destinationLatitude(trip.getDestinationLatitude())
                .destinationLongitude(trip.getDestinationLongitude())
                .tripStatus(trip.getTripStatus())
                .tripDistance(trip.getTripDistance())
                .build();
    }

}
