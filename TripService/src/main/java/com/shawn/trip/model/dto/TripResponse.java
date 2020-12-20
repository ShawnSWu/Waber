package com.shawn.trip.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TripResponse {

    private long id;

    private long matchId;

    private long driverId;

    private long passengerId;

    private long tripStatus;

    private long tripDistance;

}
