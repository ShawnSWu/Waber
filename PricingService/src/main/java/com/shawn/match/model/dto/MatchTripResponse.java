package com.shawn.match.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MatchTripResponse {

    private long id;

    private long driverId;

    private long passengerId;

    private double startPositionLatitude;

    private double startPositionLongitude;

    private long activityId;

    private long carTypeId;

    long matchStatus;

    private String date;

    private String time;

}