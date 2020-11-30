package com.shawn.match.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "activity_participant")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityParticipant {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "activity")
    private long activity;

    @Column(name = "participant")
    private long participant;

}
