package com.shawn.user.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "activity")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Activity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "extra_price")
    private long extraPrice;

    @Column(name = "start_day")
    private String startDay;

    @Column(name = "expire_day")
    private String expireDay;

    @ManyToMany(mappedBy = "activities", fetch = FetchType.LAZY)
    private List<User> users;

}
