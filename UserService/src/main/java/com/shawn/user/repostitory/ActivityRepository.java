package com.shawn.user.repostitory;

import com.shawn.user.model.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    Optional<Activity> findByName(String activityName);

    Optional<Activity> findById(long activityId);
}
