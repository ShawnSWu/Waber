package com.shawn.match.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchedResultDto {

    private boolean completed;

    private long passengerId;

    private long driverId;

    private String driverName;

}
