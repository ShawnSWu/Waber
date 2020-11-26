package com.shawn.user.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity(name = "driver")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Driver {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "email")
    private String email;

    @Column(name = "hashed_password")
    private String hashedPassword;

    @Column(name = "name")
    private String name;

    @Column(name = "car_type")
    private String carType;

}
