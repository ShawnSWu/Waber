package com.shawn.match.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MatchPreferredConditionDto {

    @JsonProperty("carType")
    private String carType;

    @JsonProperty("activity")
    private String activity;
}
