package com.shawn.user.model;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Entity(name = "passenger")
@AllArgsConstructor
@NoArgsConstructor
public class Passenger {

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
}
