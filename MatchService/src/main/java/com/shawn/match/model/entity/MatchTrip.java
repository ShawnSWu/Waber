package com.shawn.match.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "match_trip")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MatchTrip {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "driver_id")
    long driver;

    @Column(name = "passenger_id")
    long passenger;

    @Column(name = "start_position_latitude")
    double startPositionLatitude;

    @Column(name = "start_position_longitude")
    double startPositionLongitude;

    @Column(name = "destination_position_latitude")
    double destinationPositionLatitude;

    @Column(name = "destination_position_longitude")
    double destinationPositionLongitude;

    @Column(name = "activity_id")
    long activityId;

    @Column(name = "car_type_id")
    long carTypeId;

    @Column(name = "match_status")
    long matchStatus;

    @Column(name = "distance")
    long distance;

    @Column(name = "date")
    Date date;

    @Column(name = "time")
    Date time;

}
