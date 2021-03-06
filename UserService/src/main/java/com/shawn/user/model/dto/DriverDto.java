package com.shawn.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DriverDto {

    private long id;

    private String email;

    private String name;

    private long carTypeId;
}
