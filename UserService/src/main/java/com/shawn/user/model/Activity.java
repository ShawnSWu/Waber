package com.shawn.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "activity")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Activity {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "start_day")
    private String startDay;

    @Column(name = "expire_day")
    private String expireDay;

}
