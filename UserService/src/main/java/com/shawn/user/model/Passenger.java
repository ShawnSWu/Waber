package com.shawn.user.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Builder
@Getter
@Entity(name = "passenger")
@AllArgsConstructor
@NoArgsConstructor
public class Passenger {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "email")
    private String email;

    @Column(name = "hashed_password")
    private String hashedPassword;

    @Column(name = "name")
    private String name;
}
