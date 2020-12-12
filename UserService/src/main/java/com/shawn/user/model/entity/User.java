package com.shawn.user.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@Entity(name = "user")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "email")
    private String email;

    @Column(name = "hashed_password")
    private String hashedPassword;

    @Column(name = "name")
    private String name;

    @Column(name = "role")
    private long role;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "activity_driver", joinColumns = {@JoinColumn(name = "activity_id")},
            inverseJoinColumns = {@JoinColumn(name = "driver_id")}
    )
    private List<Activity> activities;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "driver_car_type", joinColumns = {@JoinColumn(name = "driver")},
            inverseJoinColumns = {@JoinColumn(name = "car_type_id")}
    )
    private List<CarType> carTypes;
}
