package com.shawn.match.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatchedResultResponse {

    @JsonProperty("id")
    private long id;

    @JsonProperty("completed")
    private boolean completed;

    @JsonProperty("driverName")
    private String driverName;

    @JsonProperty("passengerId")
    private long passengerId;

    @JsonProperty("date")
    private String date;

    @JsonProperty("time")
    private String time;

    @JsonProperty("driverId")
    private long driverId;

    @JsonProperty("startPositionLatitude")
    private double startPositionLatitude;

    @JsonProperty("startPositionLongitude")
    private double startPositionLongitude;

    @JsonProperty("destinationLatitude")
    private double destinationLatitude;

    @JsonProperty("destinationLongitude")
    private double destinationLongitude;

    @JsonProperty("carTypeId")
    private long carTypeId;

    @JsonProperty("activityId")
    private long activityId;

}
