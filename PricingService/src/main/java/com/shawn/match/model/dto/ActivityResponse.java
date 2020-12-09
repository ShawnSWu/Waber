package com.shawn.match.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityResponse {

    private long id;

    private String name;

    private long extraPrice;

    private String startDay;

    private String expireDay;
}
