package com.shawn.user.repostitory;

import com.shawn.user.model.entity.ActivityDriver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityDriverRepository extends JpaRepository<ActivityDriver, Long> {

    Optional<ActivityDriver> findByActivityIdAndDriverId(long activityId, long driverId);

    List<ActivityDriver> findAllByActivityIdAndCarTypeId(long activityId, long carTypeId);

    List<ActivityDriver> findAllByActivityId(long activityId);
}
