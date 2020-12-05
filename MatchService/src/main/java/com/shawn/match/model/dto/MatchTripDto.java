package com.shawn.match.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchTripDto {

    String matchId;

    long driver;

    long passenger;

    double startPositionLatitude;

    double startPositionLongitude;

    double destinationLatitude;

    double destinationLongitude;

    long activity;

    long matchStatus;

    String date;

    String time;

}
