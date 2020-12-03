package com.shawn.match.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "activity_driver")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDriver {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "activity")
    private long activity;

    @Column(name = "driver_id")
    private long driverId;

    @Column(name = "car_type")
    private String carType;

}
