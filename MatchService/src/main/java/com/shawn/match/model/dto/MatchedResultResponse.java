package com.shawn.match.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchedResultResponse {

    private long id;

    private boolean completed;

    private String driverName;

    private long passengerId;

    private String date;

    private String time;

    private long driverId;

}
