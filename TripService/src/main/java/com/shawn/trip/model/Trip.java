package com.shawn.trip.model;

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

    @Column(name = "driver_id")
    private long driverId;

    @Column(name = "passenger_id")
    private long passengerId;

    @Column(name = "date")
    private Date date;

    @Column(name = "time")
    private Date time;

    @Column(name = "start_position_latitude")
    private double startPositionLatitude;

    @Column(name = "start_position_longitude")
    private double startPositionLongitude;

    @Column(name = "destination_latitude")
    private double destinationLatitude;

    @Column(name = "destination_longitude")
    private double destinationLongitude;

    @Column(name = "trip_status")
    private long tripStatus;

}
