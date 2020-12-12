package com.shawn.user.model.entity;

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

    @Column(name = "activity_id")
    private long activityId;

    @Column(name = "driver_id")
    private long driverId;

    @Column(name = "car_type_id")
    private long carTypeId;

}
