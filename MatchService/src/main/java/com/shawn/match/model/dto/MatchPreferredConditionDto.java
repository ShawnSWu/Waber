package com.shawn.match.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MatchPreferredConditionDto {

    private LocationDto startLocation;

    private String carType;

    private String activityName;
}
