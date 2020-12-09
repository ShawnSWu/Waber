package com.shawn.user.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Getter
@Entity(name = "driver_car_type")
@AllArgsConstructor
@NoArgsConstructor
public class DriverCarType {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "driver")
    private long driver;

    @Column(name = "car_type_id")
    private long carTypeId;
}
