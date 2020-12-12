package com.shawn.user.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@Entity(name = "car_type")
@AllArgsConstructor
@NoArgsConstructor
public class CarType {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "type")
    private String type;

    @Column(name = "extra_price")
    private long extraPrice;

    @ManyToMany(mappedBy = "carTypes")
    private List<User> users;

}