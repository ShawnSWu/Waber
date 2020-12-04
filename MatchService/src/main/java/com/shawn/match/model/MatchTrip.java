package com.shawn.match.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Column(name = "activity_id")
    long activity;

    @Column(name = "match_status")
    long matchStatus;

}
