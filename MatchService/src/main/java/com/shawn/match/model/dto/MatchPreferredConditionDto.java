package com.shawn.match.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MatchPreferredConditionDto {

    private String carType;

    private String activity;

    private double destinationLatitude;

    private double destinationLongitude;
}
