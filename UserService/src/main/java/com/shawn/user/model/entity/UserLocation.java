package com.shawn.user.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "user_location")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLocation {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "datetime")
    private Date datetime;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;


}
