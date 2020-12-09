package com.shawn.trip.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "trip")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Trip {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "match_id")
    private long matchId;

    @Column(name = "date")
    private Date date;

    @Column(name = "time")
    private Date time;

    @Column(name = "destination_latitude")
    private double destinationLatitude;

    @Column(name = "destination_longitude")
    private double destinationLongitude;

    @Column(name = "trip_status")
    private long tripStatus;

    @Column(name = "trip_distance")
    private long tripDistance;

}