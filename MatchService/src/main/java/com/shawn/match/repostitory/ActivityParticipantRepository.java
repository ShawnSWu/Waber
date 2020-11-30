package com.shawn.match.repostitory;

import com.shawn.match.model.ActivityParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityParticipantRepository extends JpaRepository<ActivityParticipant, Long> {

    ActivityParticipant findByActivityAndParticipant(long activityId, long driverId);
}
