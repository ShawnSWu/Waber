package com.shawn.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "activity_participant")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityParticipant {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "activity")
    private long activity;

    @Column(name = "participant")
    private long participant;

}
