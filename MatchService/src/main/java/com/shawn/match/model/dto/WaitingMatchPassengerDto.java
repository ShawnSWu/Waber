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
public class WaitingMatchPassengerDto {

    @JsonProperty("passenger")
    private long passenger;

    @JsonProperty("preferActivity")
    private long preferActivity;

    @JsonProperty("preferCarType")
    private String preferCarType;

    @JsonProperty("destinationLatitude")
    private double destinationLatitude;

    @JsonProperty("destinationLongitude")
    private double destinationLongitude;
}
