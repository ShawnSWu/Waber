package com.shawn.match.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WaitingMatchPassengerDto {

    private long passenger;

    private long preferActivity;

    private String preferCarType;

    private double destinationLatitude;

    private double destinationLongitude;
}
