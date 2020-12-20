package com.shawn.trip.service;

import com.shawn.trip.model.dto.PickUpResponse;
import com.shawn.trip.model.dto.TripResponse;
import org.springframework.stereotype.Service;

@Service
public interface TripService {

    PickUpResponse pickUpTrip(long passengerId, long matchId);

    void arriveDestination(long passengerId, long matchId, long tripId);

    TripResponse getTrip(long passengerId, long matchId, long tripId);
}
