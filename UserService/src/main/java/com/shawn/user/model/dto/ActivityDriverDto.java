package com.shawn.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDriverDto {

    private long id;

    private long activityId;

    private long driverId;

    private long carTypeId;

}
