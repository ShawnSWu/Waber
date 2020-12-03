package com.shawn.match.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DriverDto {

    private long id;

    private String email;

    private String name;

    private String carType;

}
