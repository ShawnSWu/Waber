package com.shawn.match.repostitory;

import com.shawn.match.model.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    Activity findByName(String activityName);

    Activity findById(long activityId);
}
