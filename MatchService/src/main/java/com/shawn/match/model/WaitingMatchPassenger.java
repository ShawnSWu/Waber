package com.shawn.match.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "waiting_match_passenger")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WaitingMatchPassenger {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "passenger")
    private long passenger;

    @Column(name = "prefer_activity")
    private long preferActivity;

    @Column(name = "prefer_car_type")
    private String preferCarType;

}
