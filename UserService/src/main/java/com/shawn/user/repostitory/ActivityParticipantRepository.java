package com.shawn.user.repostitory;

import com.shawn.user.model.ActivityParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityParticipantRepository extends JpaRepository<ActivityParticipant, Long> {

    ActivityParticipant findByActivityAndParticipant(long activityId, long driverId);
}
