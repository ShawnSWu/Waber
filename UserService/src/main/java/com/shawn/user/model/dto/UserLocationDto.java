package com.shawn.user.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLocationDto {

    private long userId;

    private double latitude;

    private double longitude;
}
