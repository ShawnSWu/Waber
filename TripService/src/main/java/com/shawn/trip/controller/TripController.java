package com.shawn.trip.controller;

import com.shawn.trip.model.dto.PickUpResponse;
import com.shawn.trip.model.dto.TripResponse;
import com.shawn.trip.service.TripService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping("/users/{passengerId}/match/{matchId}/trip")
    public PickUpResponse pickUpTrip(@PathVariable long passengerId, @PathVariable long matchId) {
        return tripService.pickUpTrip(passengerId, matchId);
    }

    @PostMapping("/users/{passengerId}/match/{matchId}/trip/{tripId}/arrive")
    public void arriveDestination(@PathVariable long passengerId, @PathVariable long matchId, @PathVariable long tripId) {
        tripService.arriveDestination(passengerId, matchId, tripId);
    }

    @GetMapping("/users/{passengerId}/match/{matchId}/trip/{tripId}")
    public TripResponse getTrip(@PathVariable long passengerId, @PathVariable long matchId, @PathVariable long tripId) {
        return tripService.getTrip(passengerId, matchId, tripId);
    }
}