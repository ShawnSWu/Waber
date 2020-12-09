package com.shawn.match.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchTripDto {

    @JsonProperty("matchId")
    String matchId;

    @JsonProperty("driver")
    long driver;

    @JsonProperty("passenger")
    long passenger;

    @JsonProperty("startPositionLatitude")
    double startPositionLatitude;

    @JsonProperty("startPositionLongitude")
    double startPositionLongitude;

    @JsonProperty("destinationLatitude")
    double destinationLatitude;

    @JsonProperty("destinationLongitude")
    double destinationLongitude;

    @JsonProperty("activity")
    long activity;

    @JsonProperty("matchStatus")
    long matchStatus;

    @JsonProperty("date")
    String date;

    @JsonProperty("time")
    String time;

}
