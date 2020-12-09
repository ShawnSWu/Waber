package com.shawn.trip.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArriveDestinationReq {

    private long distance;

    @JsonProperty("location")
    private LocationDto locationDto;

}

